import {Injectable} from "@angular/core";
import {GobiiFileItem} from "../../model/gobii-file-item";
import {FileModelState, FileModelTreeEvent} from "../../model/file-model-tree-event";
import {FileModelNode, ExtractorItemType, ExtractorCategoryType, CardinalityType} from "../../model/file-model-node";
import {GobiiExtractFilterType} from "../../model/type-extractor-filter";
import {EntityType, EntitySubType} from "../../model/type-entity";
import {CvFilterType} from "../../model/cv-filter-type";
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';
import {ProcessType} from "../../model/type-process";
import {Labels} from "../../views/entity-labels";
import {DtoHeaderResponse} from "../../model/dto-header-response";
import {HeaderStatusMessage} from "../../model/dto-header-status-message";
import {TreeStatusNotification} from "../../model/tree-status-notification";
import {CardinalityExpression} from "../../model/cardinality-expression";
import {ModelTreeValidationError} from "../../model/model-tree-validation-error";


@Injectable()
export class FileModelTreeService {


    constructor() {
    }

    fileModelNodeTree: Map < GobiiExtractFilterType, Array < FileModelNode >> =
        new Map<GobiiExtractFilterType,Array<FileModelNode>>();

    cardinalityExpressions: Map<CardinalityType,CardinalityExpression> =
        new Map<CardinalityType,CardinalityExpression>();

    private validateModel(): boolean {

        //When this method is implemented, it will confirm that the sturcture of the tree is correct;
        //this is not just an intellectual exercise; the findFileModelNode() method (and probably others)
        //make important assumptions about the structure of the model. These are the assumptions:
        // 0) There is one, and only one, FileModelNode per EntityType and CvFilter value
        // there are a bunch of other rules pertaining to how tree nodes are associated with FileModelNodes,
        // but since that's in the presentation department, you'll see that over there in in the status tree.

        return true;

    }

    private getFileItemsFromModel(fileModelNodes: FileModelNode[]): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];

        fileModelNodes.forEach(currentModelNode => {

            if (currentModelNode.getChildren().length > 0) {
                let childFileItems: GobiiFileItem[] = this.getFileItemsFromModel(currentModelNode.getChildren());
                returnVal = returnVal.concat(childFileItems);
            } else {
                returnVal = returnVal.concat(currentModelNode.getFileItems());
            }

        });


        return returnVal;

    } //

    private getFileModelNodes(gobiiExtractFilterType: GobiiExtractFilterType): FileModelNode[] {


        if (this.fileModelNodeTree.size === 0) {

            // ************ SET UP VALIDATORS
            this.cardinalityExpressions.set(
                CardinalityType.ZERO_OR_MORE,
                new CardinalityExpression(CardinalityType.ZERO_OR_MORE,
                    n => {
                        return n > -1
                    },
                    "There must be zero or more items")
            )

            this.cardinalityExpressions.set(
                CardinalityType.ZERO_OR_ONE,
                new CardinalityExpression(CardinalityType.ZERO_OR_ONE,
                    n => {
                        return n < 2;
                    },
                    "There must be no more than one items ")
            )


            this.cardinalityExpressions.set(
                CardinalityType.ONE_ONLY,
                new CardinalityExpression(CardinalityType.ONE_ONLY,
                    n => {
                        return n == 1
                    },
                    "There must be exactly one item")
            )

            this.cardinalityExpressions.set(
                CardinalityType.MORE_THAN_ONE,
                new CardinalityExpression(CardinalityType.MORE_THAN_ONE,
                    n => {
                        return n > 1
                    },
                    "There must more than one item")
            )

            this.cardinalityExpressions.set(
                CardinalityType.ONE_OR_MORE,
                new CardinalityExpression(CardinalityType.ONE_OR_MORE,
                    n => {
                        return n >= 1
                    },
                    "There must be one or more items")
            )


            // **** FOR ALL EXTRACTION TYPES **********************************************************************
            // **** THESE ARE ALL ROOT LEVEL NODES
            let submissionItemsForAll: FileModelNode[] = [];
            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.JOB_ID, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID])
                .setCardinality(CardinalityType.ONE_ONLY)
                .setRequired(true)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Contacts)
                .setEntitySubType(EntitySubType.CONTACT_SUBMITED_BY)
                .setEntityName(Labels.instance().entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
                .setCardinality(CardinalityType.ONE_ONLY)
                .setRequired(true)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.EXPORT_FORMAT, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setCardinality(CardinalityType.ONE_ONLY)
                .setRequired(true)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Mapsets)
                .setEntityName(Labels.instance().entityNodeLabels[EntityType.Mapsets])
                .setCardinality(CardinalityType.ZERO_OR_ONE)
                .setRequired(false)
            );


            // **** SET UP EXTRACT BY DATASET  **********************************************************************
            // -- Data set type
            let submissionItemsForDataSet: FileModelNode[] = [];
            submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
            submissionItemsForDataSet.push(
//                FileModelNode.build(ExtractorItemType.CATEGORY, null)
                FileModelNode.build(ExtractorItemType.ENTITY, null)
                    .setCategoryType(ExtractorCategoryType.CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setEntityName(Labels.instance().entityNodeLabels[EntityType.DataSets])
                    .setCardinality(CardinalityType.ONE_OR_MORE)
                    .setRequired(false)
            );

            this.fileModelNodeTree.set(GobiiExtractFilterType.WHOLE_DATASET, submissionItemsForDataSet);

            // **** SET UP EXTRACT BY SAMPLES  **********************************************************************
            // -- Data set type
            let submissionItemsForBySample: FileModelNode[] = [];
            submissionItemsForBySample = submissionItemsForBySample.concat(submissionItemsForAll);
            submissionItemsForBySample.push(
                FileModelNode.build(ExtractorItemType.ENTITY, null)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(Labels.instance().cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
                    .setRequired(false)
            );

            // -- Sample List Type
            submissionItemsForBySample.push(FileModelNode.build(ExtractorItemType.SAMPLE_LIST_TYPE, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_TYPE])
                .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_TYPE])
                .setCardinality(CardinalityType.ONE_ONLY)
                .setRequired(true)
            );

            // -- Platforms
            submissionItemsForBySample.push(FileModelNode.build(ExtractorItemType.ENTITY, null)
                .setCategoryType(ExtractorCategoryType.CONTAINER)
                .setEntityType(EntityType.Platforms)
                .setEntityName(Labels.instance().entityNodeLabels[EntityType.Platforms])
                .setCardinality(CardinalityType.ZERO_OR_MORE)
                .setRequired(false)
            );

            // -- Samples Criteria
            let currentParent: FileModelNode = null;
            submissionItemsForBySample
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.ENTITY, null)
                        .setCategoryType(ExtractorCategoryType.CONTAINER)
                        .setEntityName("Sample Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                        .setRequired(false)
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Contacts)
                            .setEntitySubType(EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR)
                            .setEntityName(Labels.instance().entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR])
                            .setCardinality(CardinalityType.ONE_ONLY)
                            .setRequired(false)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Projects)
                            .setEntityName(Labels.instance().entityNodeLabels[EntityType.Projects])
                            .setCardinality(CardinalityType.ONE_OR_MORE)
                            .setRequired(false)
                        ).addChild(FileModelNode.build(ExtractorItemType.SAMPLE_FILE, currentParent)
                        .setCategoryType(ExtractorCategoryType.LEAF)
                        .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE])
                        .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_FILE])
                        .setCardinality(CardinalityType.ONE_ONLY)
                        .setRequired(false)
                    ).addChild(FileModelNode.build(ExtractorItemType.SAMPLE_LIST_ITEM, currentParent)
                        .setCategoryType(ExtractorCategoryType.CONTAINER)
                        .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM])
                        .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST_ITEM])
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setRequired(false)
                    ));

            this.fileModelNodeTree
                .set(GobiiExtractFilterType.BY_SAMPLE,
                    submissionItemsForBySample
                );


            // **** SET UP EXTRACT BY MARKERS  **********************************************************************
            let submissionItemsForByMarkers: FileModelNode[] = [];
            submissionItemsForByMarkers = submissionItemsForByMarkers.concat(submissionItemsForAll);
            submissionItemsForByMarkers.push(
                FileModelNode.build(ExtractorItemType.ENTITY, null)
                    .setCategoryType(ExtractorCategoryType.LEAF)
                    .setEntityType(EntityType.CvTerms)
                    .setCvFilterType(CvFilterType.DATASET_TYPE)
                    .setEntityName(Labels.instance().cvFilterNodeLabels[CvFilterType.DATASET_TYPE])
                    .setCardinality(CardinalityType.ONE_ONLY)
                    .setRequired(false)
            );

            // the validation algorithm effectively OR's the children: thus the children
            // are ONE_OR_MORE
            submissionItemsForByMarkers
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.ENTITY, null)
                        .setCategoryType(ExtractorCategoryType.CONTAINER)
                        .setEntityName("Markers Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Platforms])
                        .setRequired(true)
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.CONTAINER)
                            .setEntityType(EntityType.Platforms)
                            .setEntityName(Labels.instance().entityNodeLabels[EntityType.Platforms])
                            .setCardinality(CardinalityType.ONE_OR_MORE)
                            .setRequired(false)
                        ).addChild(FileModelNode.build(ExtractorItemType.MARKER_FILE, currentParent)
                        .setCategoryType(ExtractorCategoryType.LEAF)
                        .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE])
                        .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_FILE])
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setRequired(false)
                    ).addChild(FileModelNode.build(ExtractorItemType.MARKER_LIST_ITEM, currentParent)
                        .setCategoryType(ExtractorCategoryType.CONTAINER)
                        .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM])
                        .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST_ITEM])
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setRequired(false)
                    )
                );


            this.fileModelNodeTree
                .set(GobiiExtractFilterType.BY_MARKER,
                    submissionItemsForByMarkers
                );


            if (this.validateModel() == true) {
                this.subjectTreeStateNotifications.next(new TreeStatusNotification(GobiiExtractFilterType.UNKNOWN, FileModelState.READY, null));
            } else {
                //raise major warning.
            }


        }

        return this.fileModelNodeTree.get(gobiiExtractFilterType);
    }

    private validateTree(gobiiExtractFilterType: GobiiExtractFilterType): ModelTreeValidationError[] {

        let returnVal = [];

        let fileModelNodes: FileModelNode[] = this.fileModelNodeTree.get(gobiiExtractFilterType);

        // we only iterate the first level because we just happen to know, for this purpose,
        // that the tree only has two levels; if we add more, this must change
        fileModelNodes.forEach(currentFileModelNode => {

            if (currentFileModelNode.getCategoryType() === ExtractorCategoryType.LEAF ||
                (currentFileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER
                && currentFileModelNode.getChildren().length === 0 )) {

                let currentCardinalityExpression = this.cardinalityExpressions
                    .get(currentFileModelNode.getCardinality());

                let fileItemCount: number = currentFileModelNode.getFileItems().length;

                if (!currentCardinalityExpression.isValid(fileItemCount)) {

                    returnVal.push(new ModelTreeValidationError(
                        currentCardinalityExpression.message,
                        currentFileModelNode
                    ));
                }

            } else if (currentFileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {

                let atLeastOneIsSatisfied: boolean = false;
                for (let idx: number = 0;
                     ( idx < currentFileModelNode.getChildren().length) && (atLeastOneIsSatisfied === false);
                     idx++) {

                    let currentFileModelNodeChild = currentFileModelNode.getChildren()[idx];

                    let currentChildCardinalityExpression = this.cardinalityExpressions
                        .get(currentFileModelNodeChild.getCardinality());

                    let childFileItemCount: number = currentFileModelNodeChild.getFileItems().length;
                    atLeastOneIsSatisfied = currentChildCardinalityExpression .isValid(childFileItemCount);
                    let validatorIsWRONG: boolean = atLeastOneIsSatisfied;

                }

                if (atLeastOneIsSatisfied === false) {

                    currentFileModelNode.getChildren().forEach(currentChildNode => {

                        let currentChildCardinalityExpression = this.cardinalityExpressions
                            .get(currentChildNode.getCardinality());


                        returnVal.push(new ModelTreeValidationError(
                            currentChildCardinalityExpression.message,
                            currentChildNode));
                    });

                } // if none of the required children are present

            } else {
                ; // we don't have a type like this yet?
            } // if-else the model node has child model nodes

        });// for each first level node

        return returnVal;
    }

    private processNotification(fileItem: GobiiFileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = new FileModelTreeEvent(fileItem, null, null, null);
        return returnVal;

    }

    private mutate(fileItem: GobiiFileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = null;

        if (fileItem.getGobiiExtractFilterType() != GobiiExtractFilterType.UNKNOWN) {

            let fileModelNode: FileModelNode = this.findFileModelNode(fileItem.getGobiiExtractFilterType(),
                fileItem);

            if (fileModelNode != null) {

                if (fileItem.getProcessType() === ProcessType.CREATE || fileItem.getProcessType() === ProcessType.UPDATE) {

                    this.placeNodeInModel(fileModelNode, fileItem);


                    // this condition is going to required further thought . . .
                    // you have to if cardiality of aprent is ONE_OR_MORE, then
                    // you have to check for siblings. Not sure how complex we
                    // need to make this
                    if (( fileModelNode.getCardinality() === CardinalityType.ONE_OR_MORE
                        || fileModelNode.getCardinality() === CardinalityType.ONE_ONLY
                        || fileModelNode.getCardinality() === CardinalityType.MORE_THAN_ONE)
                        && fileModelNode.getCategoryType() != ExtractorCategoryType.CONTAINER) {

                        if (fileModelNode.getParent() == null) {

                            fileItem.setRequired(true);
                        }

                    }

                    returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.SUBMISSION_INCOMPLETE, null);

                } else if (fileItem.getProcessType() === ProcessType.DELETE) {

                    if( this.removeFromModel(fileModelNode, fileItem) ) {

                        returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.SUBMISSION_INCOMPLETE, null);

                    } else {
                        let message:string = "The specified file item could not be removed because it does not exist in the model";

                        if(fileModelNode && fileModelNode.getCategoryName() ) {
                            message += "; model category: " + fileModelNode.getCategoryName();
                        }
                        if(fileItem && fileItem.getItemName() ) {
                            message += "; fileitem name: " + fileItem.getItemName();
                        }

                        returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.ERROR, message);
                    }

                } else {
                    returnVal = new FileModelTreeEvent(fileItem,
                        null,
                        FileModelState.ERROR,
                        "Unhandled file item process type: " + ProcessType[fileItem.getProcessType()]);
                }


            } else {


                // this condition deals with a design flaw: not all entity types are handled for all
                // extract filter types; this is not an error, but we want to validate that the entity
                // does match at least one exractor type; if it does, then we mark it as a mismatch
                // and subscribers know to ignore this type of event; in the future, if we want the
                // status tree to maintain state across extraction filter types, this could be useful

                let remainingExtractorTypes: GobiiExtractFilterType[] = [GobiiExtractFilterType.WHOLE_DATASET,
                    GobiiExtractFilterType.BY_SAMPLE,
                    GobiiExtractFilterType.BY_MARKER];

                remainingExtractorTypes.splice(remainingExtractorTypes.indexOf(fileItem.getGobiiExtractFilterType()), 1);

                let fileModelNode: FileModelNode = null;
                for (let idx: number = 0; idx < remainingExtractorTypes.length && fileModelNode == null; idx++) {

                    let currentGobiiExtractFilterType: GobiiExtractFilterType = remainingExtractorTypes[idx];
                    fileModelNode = this.findFileModelNode(currentGobiiExtractFilterType, fileItem);
                    if (fileModelNode != null) {
                        fileItem.setGobiiExtractFilterType(currentGobiiExtractFilterType);
                    }
                }

                if (fileModelNode != null) {

                    returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.MISMATCHED_EXTRACTOR_FILTER_TYPE, null);

                } else {

                    returnVal = new FileModelTreeEvent(fileItem,
                        null,
                        FileModelState.ERROR,
                        "Unable to find a FileModelNode for fileItem in any extractor type tree");
                }

            } // if else found a file mode node for the file item's specified extractor filter type
        } else {
            returnVal = new FileModelTreeEvent(fileItem,
                null,
                FileModelState.ERROR,
                "An invalid extract filter type was specified");
        } // if-else extractor filter type is not set

        return returnVal;
    }


    findFileModelNode(gobiiExtractFilterType: GobiiExtractFilterType, fileItem: GobiiFileItem) {

        let fileModelNodes: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);

        let returnVal: FileModelNode = null;

        for (let idx: number = 0; ( idx < fileModelNodes.length) && (returnVal == null ); idx++) {
            let currentTemplate: FileModelNode = fileModelNodes[idx];
            returnVal = this.findTemplateByCriteria(currentTemplate,
                fileItem.getExtractorItemType(),
                fileItem.getEntityType(),
                fileItem.getEntitySubType(),
                fileItem.getCvFilterType());
        }

        return returnVal;

    }


    findFileModelNodeByUniqueId(fileModelNodes: FileModelNode[], fileModelNodeUniqueId: string): FileModelNode {

        let returnVal: FileModelNode = null;

        for (let idx: number = 0; (returnVal == null) && ( idx < fileModelNodes.length); idx++) {

            let currentNode: FileModelNode = fileModelNodes[idx];
            if (currentNode.getFileModelNodeUniqueId() === fileModelNodeUniqueId) {
                returnVal = currentNode;
            } else {
                returnVal = this.findFileModelNodeByUniqueId(currentNode.getChildren(), fileModelNodeUniqueId);
            }
        }

        return returnVal;
    }

    findTemplateByCriteria(fileModelNode: FileModelNode,
                           extractorItemType: ExtractorItemType,
                           entityType: EntityType,
                           entitySubType: EntitySubType,
                           cvFilterType: CvFilterType): FileModelNode {

        let returnVal: FileModelNode = null;

        if (fileModelNode.getChildren() != null) {

            for (let idx: number = 0; ( idx < fileModelNode.getChildren().length) && (returnVal == null ); idx++) {

                let currentTemplate: FileModelNode = fileModelNode.getChildren()[idx];
                returnVal = this.findTemplateByCriteria(currentTemplate,
                    extractorItemType,
                    entityType,
                    entitySubType,
                    cvFilterType);
            }
        }

        if (returnVal === null) {

            if (extractorItemType == fileModelNode.getItemType()
                && entityType == fileModelNode.getEntityType()
                && entitySubType == fileModelNode.getEntitySubType()
                && cvFilterType == fileModelNode.getCvFilterType()) {

                returnVal = fileModelNode;
            }
        }

        return returnVal;

    }


    private placeNodeInModel(fileModelNode: FileModelNode, fileItem: GobiiFileItem) {


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getFileItems().length === 0) {
                fileModelNode.getFileItems().push(fileItem);
            } else {
                fileModelNode.getFileItems()[0] = fileItem;
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {

            let existingItems: GobiiFileItem[] = fileModelNode.getFileItems().filter(
                item => {
                    return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                }
            )

            if (existingItems.length === 0) {
                fileModelNode.getFileItems().push(fileItem);
            } else {
                let idx: number = fileModelNode.getFileItems().indexOf(existingItems[0]);
                fileModelNode.getFileItems()[idx] = fileItem;
            }

        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }


    } //

    private removeFromModel(fileModelNode: FileModelNode, fileItem: GobiiFileItem): boolean {

        let returnVal: boolean = false;


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getFileItems()
                && fileModelNode.getFileItems().length > 0
                && fileModelNode.getFileItems()[0].getFileItemUniqueId() === fileItem.getFileItemUniqueId()) {
                returnVal = (fileModelNode.getFileItems().splice(0, 1)).length > 0;
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {

            let existingItem: GobiiFileItem = fileModelNode.getFileItems().find(
                item => {
                    return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                }
            );

            let idxOfItemToRemove: number = fileModelNode.getFileItems().indexOf(existingItem);

            returnVal = (fileModelNode.getFileItems().splice(idxOfItemToRemove, 1)).length > 0;


        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }

        return returnVal;

    }

    private subjectTreeStateNotifications: Subject < TreeStatusNotification > = new Subject<TreeStatusNotification>();

    public treeStateNotifications(): Subject < TreeStatusNotification > {
        return this.subjectTreeStateNotifications;
    }


    private subjectTreeNotifications: Subject < FileModelTreeEvent > = new Subject<FileModelTreeEvent>();

    public treeNotifications(): Subject < FileModelTreeEvent > {
        return this.subjectTreeNotifications
    }

    private subjectFileItemNotifications: Subject < GobiiFileItem > = new Subject<GobiiFileItem>();

    public fileItemNotifications(): Subject < GobiiFileItem > {
        return this.subjectFileItemNotifications
    }

    public put(fileItem: GobiiFileItem): Observable < FileModelTreeEvent > {

        return Observable.create(observer => {

            let foo: string = "foo";

            let fileTreeEvent: FileModelTreeEvent = null;

            if (fileItem.getProcessType() !== ProcessType.NOTIFY) {
                fileTreeEvent = this.mutate(fileItem);
            } else {
                fileTreeEvent = this.processNotification(fileItem);
            }

            if (fileTreeEvent.fileModelState != FileModelState.ERROR) {

                observer.next(fileTreeEvent);
                observer.complete();

                this.subjectTreeNotifications.next(fileTreeEvent);
                this.subjectFileItemNotifications.next(fileTreeEvent.fileItem);

                let modelTreeValidationErrors: ModelTreeValidationError[] = this.validateTree(fileItem.getGobiiExtractFilterType());
                let fileModelState: FileModelState = ( modelTreeValidationErrors.length === 0 ) ? FileModelState.SUBMISSION_READY : FileModelState.READY;
                let treeStatusNotification: TreeStatusNotification = new TreeStatusNotification(fileItem.getGobiiExtractFilterType(), fileModelState, modelTreeValidationErrors);
                this.subjectTreeStateNotifications.next(treeStatusNotification);


            } else {

                let headerStatusMessage: HeaderStatusMessage = new HeaderStatusMessage(
                    "Error mutating file item in file model tree service: "
                    + fileTreeEvent.message
                    + " processing file item: "
                    + JSON.stringify(fileItem, null, '\t'),
                    null,
                    null
                );

                observer.error(headerStatusMessage);
            }
        });
    }

    public getFileModel(gobiiExtractFilterType: GobiiExtractFilterType): Observable < FileModelNode[] > {

        return Observable.create(observer => {
            let nodesForFilterType: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);
            observer.next(nodesForFilterType);
            observer.complete();
        });
    }

    public getFileItems(gobiiExtractFilterType: GobiiExtractFilterType): Observable < GobiiFileItem[] > {

        return Observable.create(observer => {

            let nodesForFilterType: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);
            let fileItemsForExtractorFilterType: GobiiFileItem[] = this.getFileItemsFromModel(nodesForFilterType);
            observer.next(fileItemsForExtractorFilterType);
            observer.complete();
        });
    }

    public getFileModelNode(gobiiExtractFilterType: GobiiExtractFilterType,
                            fileModelNodeUniqueId: string): Observable < FileModelNode > {

        return Observable.create(observer => {

            let fileModelNodes: FileModelNode[] = this.fileModelNodeTree.get(gobiiExtractFilterType);
            let fileModeNode: FileModelNode = this.findFileModelNodeByUniqueId(fileModelNodes, fileModelNodeUniqueId);
            observer.next(fileModeNode);
            observer.complete();
        });
    }

    public getTreeState(gobiiExtractFilterType: GobiiExtractFilterType): Observable < TreeStatusNotification > {

        return Observable.create(observer => {

            let modelTreeValidationErrors: ModelTreeValidationError[] = this.validateTree(gobiiExtractFilterType);
            let fileModelState: FileModelState = ( modelTreeValidationErrors.length === 0 ) ? FileModelState.SUBMISSION_READY : FileModelState.READY;
            let treeStatusNotification: TreeStatusNotification = new TreeStatusNotification(gobiiExtractFilterType, fileModelState, modelTreeValidationErrors);

            observer.next(treeStatusNotification);
            observer.complete();
        });
    }
}
