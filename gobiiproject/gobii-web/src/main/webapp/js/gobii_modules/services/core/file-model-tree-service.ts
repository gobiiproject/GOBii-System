import {Injectable} from "@angular/core";
import {FileItem} from "../../model/file-item";
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


@Injectable()
export class FileModelTreeService {


    constructor() {
    }

    fileModelNodeTree: Map < GobiiExtractFilterType, Array < FileModelNode >> =
        new Map<GobiiExtractFilterType,Array<FileModelNode>>();


    private validateModel(): boolean {

        //When this method is implemented, it will confirm that the sturcture of the tree is correct;
        //this is not just an intellectual exercise; the findFileModelNode() method (and probably others)
        //make important assumptions about the structure of the model. These are the assumptions:
        // 0) There is one, and only one, FileModelNode per EntityType and CvFilter value
        // there are a bunch of other rules pertaining to how tree nodes are associated with FileModelNodes,
        // but since that's in the presentation department, you'll see that over there in in the status tree.

        return true;

    }

    private getFileItemsFromModel(fileModelNodes: FileModelNode[]): FileItem[] {

        let returnVal: FileItem[] = [];

        fileModelNodes.forEach(currentModelNode => {

            if (currentModelNode.getChildren().length > 0) {
                let childFileItems: FileItem[] = this.getFileItemsFromModel(currentModelNode.getChildren());
                returnVal = returnVal.concat(childFileItems);
            } else {
                returnVal = returnVal.concat(currentModelNode.getFileItems());
            }

        });


        return returnVal;

    } //

    private getFileModelNodes(gobiiExtractFilterType: GobiiExtractFilterType): FileModelNode[] {


        if (this.fileModelNodeTree.size === 0) {


            // **** FOR ALL EXTRACTION TYPES **********************************************************************
            // **** THESE ARE ALL ROOT LEVEL NODES
            let submissionItemsForAll: FileModelNode[] = [];
            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.ENTITY, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setEntityType(EntityType.Contacts)
                .setEntityName(Labels.instance().entitySubtypeNodeLabels[EntitySubType.CONTACT_SUBMITED_BY])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.EXPORT_FORMAT, null)
                .setCategoryType(ExtractorCategoryType.LEAF)
                .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.EXPORT_FORMAT])
                .setCardinality(CardinalityType.ONE_ONLY)
            );

            submissionItemsForAll.push(FileModelNode.build(ExtractorItemType.CATEGORY, null)
                .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                .setEntityType(EntityType.Mapsets)
                .setCategoryName(Labels.instance().entityNodeLabels[EntityType.Mapsets])
                .setCardinality(CardinalityType.ZERO_OR_ONE)
            );


            // **** SET UP EXTRACT BY DATASET  **********************************************************************
            // -- Data set type
            let submissionItemsForDataSet: FileModelNode[] = [];
            submissionItemsForDataSet = submissionItemsForDataSet.concat(submissionItemsForAll);
            submissionItemsForDataSet.push(
                FileModelNode.build(ExtractorItemType.CATEGORY, null)
                    .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                    .setEntityType(EntityType.DataSets)
                    .setCategoryName(Labels.instance().entityNodeLabels[EntityType.DataSets])
                    .setCardinality(CardinalityType.ONE_OR_MORE));

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
            );

            // -- Platforms
            let currentParent: FileModelNode = null;
            submissionItemsForBySample.push(FileModelNode.build(ExtractorItemType.CATEGORY, null)
                .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                .setEntityType(EntityType.Platforms)
                .setCategoryName(Labels.instance().entityNodeLabels[EntityType.Platforms])
                .setCardinality(CardinalityType.ZERO_OR_MORE)
            );


            // -- Samples
            submissionItemsForBySample
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.CATEGORY, null)
                        .setCategoryType(ExtractorCategoryType.MODEL_CONTAINER)
                        .setCategoryName("Sample Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Projects, EntityType.Contacts])
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.LEAF)
                            .setEntityType(EntityType.Contacts)
                            .setEntityName(Labels.instance().entitySubtypeNodeLabels[EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR])
                            .setCardinality(CardinalityType.ZERO_OR_ONE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(EntityType.Projects)
                            .setEntityName(Labels.instance().entityNodeLabels[EntityType.Projects])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.SAMPLE_LIST, currentParent)
                            .setCategoryType(ExtractorCategoryType.CATEGORY_CONTAINER)
                            .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST])
                            .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.SAMPLE_LIST])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
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
            );

            submissionItemsForByMarkers
                .push(currentParent =
                    FileModelNode.build(ExtractorItemType.CATEGORY, null)
                        .setCategoryType(ExtractorCategoryType.MODEL_CONTAINER)
                        .setCategoryName("Markers Crieria")
                        .setCardinality(CardinalityType.ONE_OR_MORE)
                        .setAlternatePeerTypes([EntityType.Platforms])
                        .addChild(FileModelNode.build(ExtractorItemType.ENTITY, currentParent)
                            .setCategoryType(ExtractorCategoryType.ENTITY_CONTAINER)
                            .setEntityType(EntityType.Platforms)
                            .setEntityName(Labels.instance().entityNodeLabels[EntityType.Platforms])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        )
                        .addChild(FileModelNode.build(ExtractorItemType.MARKER_LIST, currentParent)
                            .setCategoryType(ExtractorCategoryType.CATEGORY_CONTAINER)
                            .setEntityName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST])
                            .setCategoryName(Labels.instance().treeExtractorTypeLabels[ExtractorItemType.MARKER_LIST])
                            .setCardinality(CardinalityType.ZERO_OR_MORE)
                        ));


            this.fileModelNodeTree
                .set(GobiiExtractFilterType.BY_MARKER,
                    submissionItemsForByMarkers
                );


            if (this.validateModel() == true) {
                this.subjectTreeStateNotifications.next(new TreeStatusNotification(FileModelState.READY, null));
            } else {
                //raise major warning.
            }


        }

        return this.fileModelNodeTree.get(gobiiExtractFilterType);
    }

    private mutate(fileItem: FileItem): FileModelTreeEvent {

        let returnVal: FileModelTreeEvent = null;

        if (fileItem.getGobiiExtractFilterType() != GobiiExtractFilterType.UNKNOWN) {

            let fileModelNode: FileModelNode = this.findFileModelNode(fileItem.getGobiiExtractFilterType(), fileItem.getEntityType(), fileItem.getCvFilterType());

            if (fileItem.getProcessType() === ProcessType.CREATE || fileItem.getProcessType() === ProcessType.UPDATE) {

                this.placeNodeInModel(fileModelNode, fileItem);


                // this condition is going to required further thought . . .
                // you have to if cardiality of aprent is ONE_OR_MORE, then
                // you have to check for siblings. Not sure how complex we
                // need to make this
                if (( fileModelNode.getCardinality() === CardinalityType.ONE_OR_MORE
                    || fileModelNode.getCardinality() === CardinalityType.ONE_ONLY
                    || fileModelNode.getCardinality() === CardinalityType.MORE_THAN_ONE)
                    && fileModelNode.getCategoryType() != ExtractorCategoryType.ENTITY_CONTAINER) {

                    if (fileModelNode.getParent() == null) {

                        fileItem.setRequired(true);
                    }

                }

                returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.SUBMISSION_INCOMPLETE, null);

            } else if (fileItem.getProcessType() === ProcessType.DELETE) {

                this.removeFromModel(fileModelNode, fileItem);
                returnVal = new FileModelTreeEvent(fileItem, fileModelNode, FileModelState.SUBMISSION_INCOMPLETE, null);

            } else {
                returnVal = new FileModelTreeEvent(fileItem,
                    null,
                    FileModelState.ERROR,
                    "Unhandled file item process type: " + ProcessType[fileItem.getProcessType()]);
            }


        } else {
            returnVal = new FileModelTreeEvent(fileItem,
                null,
                FileModelState.ERROR,
                "An invalid extract filter type was specified");
        }


        return returnVal;
    }


    findFileModelNode(gobiiExtractFilterType: GobiiExtractFilterType, entityType: EntityType, cvFilter: CvFilterType) {

        let fileModelNodes: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);

        let returnVal: FileModelNode = null;

        for (let idx: number = 0; ( idx < fileModelNodes.length) && (returnVal == null ); idx++) {
            let currentTemplate: FileModelNode = fileModelNodes[idx];
            returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilter);
        }

        return returnVal;

    }


    findTemplateByCriteria(fileModelNode: FileModelNode,
                           entityType: EntityType,
                           cvFilterType: CvFilterType): FileModelNode {

        let returnVal: FileModelNode = null;

        if (fileModelNode.getChildren() != null) {

            for (let idx: number = 0; ( idx < fileModelNode.getChildren().length) && (returnVal == null ); idx++) {

                let currentTemplate: FileModelNode = fileModelNode.getChildren()[idx];
                returnVal = this.findTemplateByCriteria(currentTemplate, entityType, cvFilterType);
            }
        }

        if (returnVal === null) {

            if (entityType == fileModelNode.getEntityType() && cvFilterType == fileModelNode.getCvFilterType()) {

                returnVal = fileModelNode;
            }
        }

        return returnVal;

    }


    private placeNodeInModel(fileModelNode: FileModelNode, fileItem: FileItem) {


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getFileItems().length === 0) {
                fileModelNode.getFileItems().push(fileItem);
            } else {
                fileModelNode.getFileItems()[0] = fileItem;
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

            let existingItems: FileItem[] = fileModelNode.getFileItems().filter(
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

    private removeFromModel(fileModelNode: FileModelNode, fileItem: FileItem) {


        if (fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

            // a leaf should never have more than one
            if (fileModelNode.getFileItems()[0].getFileItemUniqueId() === fileItem.getFileItemUniqueId()) {
                fileModelNode.getFileItems().splice(0, 1);
            }

        } else if (fileModelNode.getCategoryType() === ExtractorCategoryType.ENTITY_CONTAINER) {

            let existingItem: FileItem = fileModelNode.getFileItems().find(
                item => {
                    return item.getFileItemUniqueId() === fileItem.getFileItemUniqueId();
                }
            );

            let idxOfItemToRemove: number = fileModelNode.getFileItems().indexOf(existingItem);

            fileModelNode.getFileItems().splice(idxOfItemToRemove, 1);


        } else {
            // this.reportMessage("The node of category  "
            //     + fileModelNode.getCategoryType()
            //     + " for checkbox event " + fileItemEvent.itemName
            //     + " could not be placed in the tree ");
        }

    }

    private subjectTreeStateNotifications: Subject < TreeStatusNotification > = new Subject<TreeStatusNotification>();

    public treeStateNotifications(): Subject < TreeStatusNotification > {
        return this.subjectTreeStateNotifications;
    }


    private subjectTreeNotifications: Subject < FileModelTreeEvent > = new Subject<FileModelTreeEvent>();

    public treeNotifications(): Subject < FileModelTreeEvent > {
        return this.subjectTreeNotifications
    }

    private subjectFileItemNotifications: Subject < FileItem > = new Subject<FileItem>();

    public fileItemNotifications(): Subject < FileItem > {
        return this.subjectFileItemNotifications
    }


    public put(fileItem: FileItem): Observable < FileModelTreeEvent > {

        return Observable.create(observer => {

            let foo: string = "foo";

            let fileTreeEvent: FileModelTreeEvent = this.mutate(fileItem);

            if (fileTreeEvent.fileModelState != FileModelState.ERROR) {

                observer.next(fileTreeEvent);
                observer.complete();

                this.subjectTreeNotifications.next(fileTreeEvent);
                this.subjectFileItemNotifications.next(fileTreeEvent.fileItem);
            } else {


                let headerResponse: DtoHeaderResponse = new DtoHeaderResponse(false, [new HeaderStatusMessage(
                    "Error mutating file item in file model tree service: "
                    + fileTreeEvent.message
                    + " processing file item: "
                    + fileItem.getFileItemUniqueId()
                    + " (" + (fileItem.getItemName() !== null ? fileItem.getItemName() : "unnamed" ) + ")",
                    null,
                    null
                )]);

                observer.error(headerResponse);
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

    public getFileItems(gobiiExtractFilterType: GobiiExtractFilterType): Observable < FileItem[] > {

        return Observable.create(observer => {

            let nodesForFilterType: FileModelNode[] = this.getFileModelNodes(gobiiExtractFilterType);
            let fileItemsForExtractorFilterType: FileItem[] = this.getFileItemsFromModel(nodesForFilterType);
            observer.next(fileItemsForExtractorFilterType);
            observer.complete();
        });
    }
}
