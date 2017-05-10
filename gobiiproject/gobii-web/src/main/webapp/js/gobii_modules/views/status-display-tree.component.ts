import {Component, OnInit, ViewChild, OnChanges, SimpleChange, EventEmitter} from "@angular/core";
import {TreeNode, Message, MenuItem} from "primeng/components/common/api";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiTreeNode} from "../model/GobiiTreeNode";
import {EntityType, EntitySubType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {
    FileModelNode, ExtractorItemType, ExtractorCategoryType,
    CardinalityType
} from "../model/file-model-node";
import {CvFilterType} from "../model/cv-filter-type";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {FileModelTreeEvent, FileModelState} from "../model/file-model-tree-event";
import {ProcessType} from "../model/type-process";
import {GobiiExtractFormat} from "../model/type-extract-format";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {Labels} from "./entity-labels";
import {Header} from "../model/payload/header";
import {GobiiUIEventOrigin} from "../model/type-event-origin";
import {StatusLevel} from "../model/type-status-level";


//Documentation of p-tree: http://www.primefaces.org/primeng/#/tree
//width of p-tree does not take if it's in the style sheet class; you have to inline it
@Component({
    selector: 'status-display-tree',
    inputs: ['fileItemEventChange', 'gobiiExtractFilterTypeEvent'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage', 'onTreeReady'],
    template: ` 
                    <p-tree [value]="gobiiTreeNodes" 
                    selectionMode="checkbox" 
                    propagateSelectionUp="false"
                    propagateSelectionDown="false"
                    [(selection)]="selectedGobiiNodes"
                    (onNodeUnselect)="nodeUnselect($event)"
                    (onNodeSelect)="nodeSelect($event)"
                    (onNodeExpand)="nodeExpand($event)"
                    (onNodeCollapse)="nodeCollapse($event)"
                    [style]="{'width':'100%'}"
                    styleClass="criteria-tree"></p-tree>
                    <!--<p-tree [value]="demoTreeNodes" selectionMode="checkbox" [(selection)]="selectedDemoNodes"></p-tree>-->
                    <!--<div>Selected Nodes: <span *ngFor="let file of selectedFiles2">{{file.label}} </span></div>-->
`
})
export class StatusDisplayTreeComponent implements OnInit, OnChanges {

    private containerCollapseThreshold = 10;
    private onAddMessage: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private onTreeReady: EventEmitter<HeaderStatusMessage> = new EventEmitter();

    private handleAddStatusMessage(headerStatusMessage: HeaderStatusMessage) {

        this.onAddMessage.emit(headerStatusMessage);

    }

    constructor(private _fileModelTreeService: FileModelTreeService) {

        // has to be in ctor because if you put it in ngOnInit(), there can be ngOnChange events
        // before ngOnInit() is called.
        this._fileModelTreeService
            .treeNotifications()
            .subscribe(fileModelTreeEvent => {


                if (this.treeIsInitialized) {

                    if (fileModelTreeEvent.fileModelState != FileModelState.MISMATCHED_EXTRACTOR_FILTER_TYPE) {


                        if (fileModelTreeEvent.fileItem.getProcessType() === ProcessType.CREATE
                            || fileModelTreeEvent.fileItem.getProcessType() === ProcessType.UPDATE) {
                            this.placeNodeInTree(fileModelTreeEvent);
                        } else if (fileModelTreeEvent.fileItem.getProcessType() === ProcessType.DELETE) {
                            this.removeNodeFromTree(fileModelTreeEvent);
                        } else if (fileModelTreeEvent.fileItem.getProcessType() === ProcessType.NOTIFY) {
                            if (fileModelTreeEvent.fileItem.getExtractorItemType() === ExtractorItemType.CLEAR_TREE) {
                                this.clearTree();
                            }
                        } else {

                            let headerStatusMessage: HeaderStatusMessage =
                                new HeaderStatusMessage("Error in status display tree processing file item type "
                                    + ExtractorItemType[fileModelTreeEvent.fileItem.getExtractorItemType()]
                                    + ": Unknown porcess type: "
                                    + ProcessType[fileModelTreeEvent.fileItem.getProcessType()], null, null);

                            this.handleAddStatusMessage(headerStatusMessage);
                        }
                    }

                } else {

                    let message: string = "Warning: a fileItem was posted ot the tree before the tree was initialized:  "
                        + Labels.instance().treeExtractorTypeLabels[fileModelTreeEvent.fileModelNode.getItemType()];

                    if (fileModelTreeEvent.fileItem && fileModelTreeEvent.fileItem.getItemName()) {
                        message += " for fileItem of name " + fileModelTreeEvent.fileItem.getItemName();
                    }

                    this.handleAddStatusMessage(new HeaderStatusMessage(message,
                        StatusLevel.WARNING,
                        null));
                }
            });
    }

    ngOnInit() {

        let foo: string = "bar";


        // this.makeDemoTreeNodes();
        // this.setUpRequredItems();

    }


    findRemovableFileItems(treeNode: GobiiTreeNode): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];

        if (treeNode.fileItemId
            && !treeNode.required) {
            let currentFileItem: GobiiFileItem = this.makeFileItemFromTreeNode(treeNode, ProcessType.DELETE)
                .setGobiiEventOrigin(GobiiUIEventOrigin.CRITERIA_TREE);

            if (( currentFileItem.getExtractorItemType() != ExtractorItemType.ENTITY )
                || currentFileItem.getItemId()) {
                returnVal.push(currentFileItem);
            }
        }

        if (treeNode.children) {

            treeNode.children.forEach(tn => {
                let currentItemsToRemove: GobiiFileItem[] = this.findRemovableFileItems(tn);
                returnVal.push.apply(returnVal, currentItemsToRemove);
            })
        }

        return returnVal;
    }

    clearTree() {

        let itemsToRemove: GobiiFileItem[] = [];

        this.gobiiTreeNodes.forEach(fin => {

            let childItemsToRemove: GobiiFileItem[] = this.findRemovableFileItems(fin);
            itemsToRemove.push.apply(itemsToRemove, childItemsToRemove);

        });


        let nodesToDeselect: GobiiTreeNode[] = this.selectedGobiiNodes.filter(tn => {
            return tn && !tn.required
        });

        nodesToDeselect.forEach(tn => {
            let idxOfSelectedNodeParentNode: number = this.selectedGobiiNodes.indexOf(tn);
            if (idxOfSelectedNodeParentNode >= 0) {
                let deleted: GobiiTreeNode[] = this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
            }
        })

        itemsToRemove.forEach(itr => {
            if (itr) {
                this._fileModelTreeService.put(itr).subscribe(
                    fmte => {

                    },
                    headerResponse => {
                        this.handleAddStatusMessage(headerResponse)
                    });
            }
        });


    }

// *****************************************************************
// *********************  TREE NODE DATA STRUCTURES AND EVENTS

    demoTreeNodes: TreeNode[] = [];
    selectedDemoNodes: TreeNode[] = [];


    gobiiTreeNodes: GobiiTreeNode[] = [];
    selectedGobiiNodes: GobiiTreeNode[] = [];

    experimentId: string;


    nodeSelect(event) {

        // Unless a node already is checked such that it has data, we don't allow checking
        // something because it has no meaning without data in it; these would typically
        // by CONTAINER type nodes: once they have children they're selected, and it which
        // point we deal with check events in nodeUnselect()
        // yes this is a bit of a kludge; version 4 of PrimeNG will add a selectable proeprty
        // to TreeNode which will enable us to approch selectability of nodes in general in
        // a more systematic and elegant way


        let selectedGobiiTreeNode: GobiiTreeNode = event.node;

        selectedGobiiTreeNode.children.forEach(childNode => {
            this.removeItemFromSelectedNodes(childNode);
        })

        this.removeItemFromSelectedNodes(selectedGobiiTreeNode);

    }

    getFileItemsToDeselect(parentNode: GobiiTreeNode): GobiiFileItem[] {

        let returnVal: GobiiFileItem[] = [];

        if (parentNode.fileItemId !== null) {
            let parentFileItem: GobiiFileItem = this.makeFileItemFromTreeNode(parentNode, ProcessType.DELETE);
            returnVal.push(parentFileItem);
        }


        parentNode.children.forEach(gtn => {
            if (gtn.fileItemId !== null) {
                let currentFileItem: GobiiFileItem = this.makeFileItemFromTreeNode(gtn, ProcessType.DELETE);
                returnVal.push(currentFileItem);
            }

            let childNodes: GobiiFileItem[] = this.getFileItemsToDeselect(gtn);
            returnVal.push.apply(childNodes);

        })


        // fileItem.setGobiiEventOrigin(GobiiUIEventOrigin.CRITERIA_TREE);

        returnVal.forEach(fi => {
            fi.setGobiiEventOrigin(GobiiUIEventOrigin.CRITERIA_TREE);
        })

        return returnVal;

    }


    // we need to disable partial selection because when you click
    // a node that's partially selected, you don't get the unselect event
    // which breaks everything
    unsetPartialSelect(gobiiTreeNode: GobiiTreeNode) {

        let thereAreSelectedChildren: boolean = false;
        if (gobiiTreeNode.partialSelected) {

            gobiiTreeNode.partialSelected = false;

            let foo: string = "foo";

            for (let idx: number = 0;
                 (idx < gobiiTreeNode.children.length) && !thereAreSelectedChildren; idx++) {

                let currentTreeNode: GobiiTreeNode = gobiiTreeNode.children[idx];
                thereAreSelectedChildren = this.selectedGobiiNodes.find(fi => {

                        return fi
                            && fi.fileItemId
                            && (fi.fileItemId === currentTreeNode.fileItemId)
                    }) != undefined;
            }

            if (thereAreSelectedChildren) {
                this.selectedGobiiNodes.push(gobiiTreeNode);
            }
        }

        if (( gobiiTreeNode.parent !== null )
            && ( gobiiTreeNode.parent !== undefined )) {
            this.unsetPartialSelect(gobiiTreeNode.parent);
        }

    }

    nodeUnselect(event) {

        // this funditonality is nearly working;
        // but it breaks down in the marker criteria section of the
        // tree. There is no more time to work on this. It must just
        // effectively disabled for now: you can only select and deselect
        // from the controls outside the tree
        let unselectedTreeNode: GobiiTreeNode = event.node;
        this.unsetPartialSelect(unselectedTreeNode);
        this.selectedGobiiNodes.push(unselectedTreeNode);
        unselectedTreeNode.children.forEach(tn => {
            this.selectedGobiiNodes.push(tn);
        })


        /*
         let unselectedTreeNode: GobiiTreeNode = event.node;

         if (( !unselectedTreeNode.required )) {


         let itemsToRemove: GobiiFileItem[] = this.getFileItemsToDeselect(unselectedTreeNode);

         this.unsetPartialSelect(unselectedTreeNode);

         itemsToRemove.forEach(itr => {
         this._fileModelTreeService.put(itr).subscribe(
         fmte => {

         },
         headerResponse => {
         this.handleAddStatusMessage(headerResponse)
         });
         })

         } else {
         // essentially disallow the selection
         this.selectedGobiiNodes.push(unselectedTreeNode);
         }
         */
    }

    makeFileItemFromTreeNode(gobiiTreeNode: GobiiTreeNode, processType: ProcessType): GobiiFileItem {


        let fileModelNode: FileModelNode = null;
        this._fileModelTreeService
            .getFileModelNode(this.gobiiExtractFilterType, gobiiTreeNode.fileModelNodeId)
            .subscribe(
                fmn => fileModelNode = fmn,
                hsm => this.handleAddStatusMessage(hsm));

        let fileItemFromModel: GobiiFileItem = fileModelNode
            .getFileItems()
            .find(fi => fi.getFileItemUniqueId() === gobiiTreeNode.fileItemId);

        let itemId: string = null;
        if (fileItemFromModel) {
            itemId = fileItemFromModel.getItemId();

        }

        // in theory we should be able ot just return the fileItem
        // we got from the model node. I tried this. And I set the
        // gobiiExtractFiltertime, process mode, and reuired value
        // from the tree mode. But the notification for controls
        // to deselect the item did not work. So we are only using
        // the fileitem from the model node to set the item id for
        // now. Sigh.
        let returnVal: GobiiFileItem = GobiiFileItem.build(
            this.gobiiExtractFilterType,
            processType)
            .setExtractorItemType(fileModelNode.getItemType())
            .setEntityType(gobiiTreeNode.entityType)
            .setEntitySubType(gobiiTreeNode.entitySubType)
            .setCvFilterType(gobiiTreeNode.cvFilterType)
            .setItemId(itemId)
            .setItemName(gobiiTreeNode.label)
            .setRequired(gobiiTreeNode.required);

        returnVal.setFileItemUniqueId(gobiiTreeNode.fileItemId);

        return returnVal;
    }

    nodeExpandMessage(event) {

    }

    nodeExpand(event) {
        if (event.node) {
        }
    }

    nodeCollapse(event) {
        if (event.node) {

        }
    }

    viewFile(file: TreeNode) {
        //      this.msgs.push({severity: 'info', summary: 'Node Selected with Right Click', detail: file.label});
    }

    unselectFile() {
//        this.selectedFile2 = null;
    }

    expandAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, true);
        });
    }

    collapseAll() {
        this.gobiiTreeNodes.forEach(node => {
            this.expandRecursive(node, false);
        });
    }

    addCountToContainerNode(node: TreeNode) {
        let foo: string = "foo";

        let parenPosition: number = node.label.indexOf("(");
        if (parenPosition > 0) {
            node.label = node.label.substring(0, parenPosition);
        }

        if (node.children.length > 0) {

            node.label += " (" + node.children.length + ")";

        }


    }

    expandRecursive(node: TreeNode, isExpand: boolean) {
        node.expanded = isExpand;
        if (node.children) {
            node.children.forEach(childNode => {
                this.expandRecursive(childNode, isExpand);
            });
        }
    }


// ********************************************************************************
// ********************* CHECKBOX/TREE NODE CONVERSION FUNCTIONS

    addEntityIconToNode(entityType: EntityType, cvFilterType: CvFilterType, treeNode: GobiiTreeNode) {

        if (entityType === EntityType.DataSets) {

            treeNode.icon = "fa-database";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-database";

        } else if (entityType === EntityType.Contacts) {

            treeNode.icon = "fa-user-o";
            treeNode.expandedIcon = "fa-user-o";
            treeNode.collapsedIcon = "fa-user-o";

        } else if (entityType === EntityType.Mapsets) {

            treeNode.icon = "fa-map-o";
            treeNode.expandedIcon = "fa-map-o";
            treeNode.collapsedIcon = "fa-map-o";

        } else if (entityType === EntityType.Platforms) {

            treeNode.icon = "fa-calculator";
            treeNode.expandedIcon = "fa-calculator";
            treeNode.collapsedIcon = "fa-calculator";

        } else if (entityType === EntityType.Projects) {

            treeNode.icon = "fa-clipboard";
            treeNode.expandedIcon = "fa-clipboard";
            treeNode.collapsedIcon = "fa-clipboard";

        } else if (entityType === EntityType.CvTerms) {

            if (cvFilterType === CvFilterType.DATASET_TYPE) {
                treeNode.icon = "fa-file-excel-o";
                treeNode.expandedIcon = "fa-file-excel-o";
                treeNode.collapsedIcon = "fa-file-excel-o";
            }

        }
    }


    addIconsToNode(statusTreeTemplate: FileModelNode, treeNode: GobiiTreeNode, isParent: boolean) {

        // if( fileModelNode.getItemType() == ExtractorItemType.ENTITY ) {

        if (statusTreeTemplate.getEntityType() != null
            && statusTreeTemplate.getEntityType() != EntityType.UNKNOWN) {

            this.addEntityIconToNode(statusTreeTemplate.getEntityType(), statusTreeTemplate.getCvFilterType(), treeNode);

        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.EXPORT_FORMAT) {
            treeNode.icon = "fa-columns";
            treeNode.expandedIcon = "fa-columns";
            treeNode.collapsedIcon = "fa-columns";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.SAMPLE_FILE) {
            treeNode.icon = "fa-file-text-o";
            treeNode.expandedIcon = "fa-file-text-o";
            treeNode.collapsedIcon = "fa-file-text-o";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.SAMPLE_LIST_ITEM) {
            if (isParent) {
                treeNode.icon = "fa-list-ul";
                treeNode.expandedIcon = "fa-list-ul";
                treeNode.collapsedIcon = "fa-list-ul";
            } else {
                treeNode.icon = "fa-eyedropper";
                treeNode.expandedIcon = "fa-eyedropper";
                treeNode.collapsedIcon = "fa-eyedropper";
            }
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.MARKER_FILE) {
            treeNode.icon = "fa-file-text-o";
            treeNode.expandedIcon = "fa-file-text-o";
            treeNode.collapsedIcon = "fa-file-text-o";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.MARKER_LIST_ITEM) {

            if (isParent) {
                treeNode.icon = "fa-list-ul";
                treeNode.expandedIcon = "fa-list-ul";
                treeNode.collapsedIcon = "fa-list-ul";
            } else {
                treeNode.icon = "fa-map-marker";
                treeNode.expandedIcon = "fa-map-marker";
                treeNode.collapsedIcon = "fa-map-marker";
            }
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.JOB_ID) {
            treeNode.icon = "fa-info-circle";
            treeNode.expandedIcon = "fa-info-circle";
            treeNode.collapsedIcon = "fa-info-circle";
        } else if (statusTreeTemplate.getItemType() === ExtractorItemType.SAMPLE_LIST_TYPE) {
            treeNode.icon = "fa-info-circle";
            treeNode.expandedIcon = "fa-info-circle";
            treeNode.collapsedIcon = "fa-info-circle";
        } else {
            //     }
            // } else if (fileModelNode.getItemType() == ExtractorItemType.CATEGORY ) {
            treeNode.icon = "fa-folder";
            treeNode.expandedIcon = "fa-folder-expanded";
            treeNode.collapsedIcon = "fa-folder";
        }
    }


    addEntityNameToNode(fileModelNode: FileModelNode, gobiiTreeNode: GobiiTreeNode, eventedFileItem: GobiiFileItem) {

        if (fileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {
            gobiiTreeNode.label = eventedFileItem.getItemName();
        } else { // coves the LEAF node use case
            if (eventedFileItem.getExtractorItemType() == ExtractorItemType.EXPORT_FORMAT) {
                let gobiiExtractFormat: GobiiExtractFormat = <GobiiExtractFormat> GobiiExtractFormat[eventedFileItem.getItemId()];
                gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + Labels.instance().extractFormatTypeLabels[gobiiExtractFormat];
            } else if (eventedFileItem.getExtractorItemType() == ExtractorItemType.JOB_ID) {
                gobiiTreeNode.label = Labels.instance().treeExtractorTypeLabels[ExtractorItemType.JOB_ID]
                    + ": " + eventedFileItem.getItemId();
            } else {
                if (eventedFileItem.getProcessType() !== ProcessType.DELETE) {
                    gobiiTreeNode.label = fileModelNode.getEntityName() + ": " + eventedFileItem.getItemName();
                } else {
                    gobiiTreeNode.label = fileModelNode.getEntityName();
                }
            }
        }
    }

    findTreeNodebyModelNodeId(gobiiTreeNodes: GobiiTreeNode[], fileModelNodeId: String): GobiiTreeNode {

        let foo: string = "bar";
        let returnVal: GobiiTreeNode = null;


        for (let idx: number = 0; (idx < gobiiTreeNodes.length) && (returnVal === null); idx++) {
            let currentTreeNode: GobiiTreeNode = gobiiTreeNodes[idx];

            if (currentTreeNode.fileModelNodeId === fileModelNodeId) {
                returnVal = currentTreeNode;
            } else {

                returnVal = this.findTreeNodebyModelNodeId(currentTreeNode.children, fileModelNodeId);
            }
        } // iterate gobii nodes


        return returnVal;
    }

    findTreeNodebyFileItemUniqueId(gobiiTreeNodes: GobiiTreeNode[], fileItemId: String): GobiiTreeNode {

        let returnVal: GobiiTreeNode = null;

        for (let idx: number = 0; (idx < gobiiTreeNodes.length) && (returnVal === null); idx++) {
            let currentTreeNode: GobiiTreeNode = gobiiTreeNodes[idx];

            if (currentTreeNode.fileItemId === fileItemId) {
                returnVal = currentTreeNode;
            } else {

                returnVal = this.findTreeNodebyFileItemUniqueId(currentTreeNode.children, fileItemId);
            }
        }

        return returnVal;
    }

    removeItemFromSelectedNodes(gobiiTreeNode: GobiiTreeNode) {

        // let selectedNode: GobiiTreeNode = this.selectedGobiiNodes.find(stn => {
        //     return stn.fileItemId === gobiiTreeNode.fileItemId
        // });

//        if (selectedNode) {
        if (gobiiTreeNode) {

            let idxOfSelectedNodeParentNode: number = this.selectedGobiiNodes.indexOf(gobiiTreeNode);
            if (idxOfSelectedNodeParentNode >= 0) {
                let deleted: GobiiTreeNode[] = this.selectedGobiiNodes.splice(idxOfSelectedNodeParentNode, 1);
                let foo: string = "foo";
            }
        }


    }

    removeNodeFromTree(fileModelTreeEvent: FileModelTreeEvent) {
        if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {

            if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

                let gobiiTreeNodeToBeRemoved: GobiiTreeNode = this.findTreeNodebyFileItemUniqueId(this.gobiiTreeNodes, fileModelTreeEvent.fileItem.getFileItemUniqueId());

                if (gobiiTreeNodeToBeRemoved !== null) {

                    // will need a funciton to do this correctly
                    this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, gobiiTreeNodeToBeRemoved, fileModelTreeEvent.fileItem);
                    this.removeItemFromSelectedNodes(gobiiTreeNodeToBeRemoved);
                } else {
                    // error node not found?
                } // if-else we found an existing node for the LEAF node's file item


            } else if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {

                // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                let parentTreeNode: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                if (parentTreeNode != null) {


                    let nodeToDelete: GobiiTreeNode = parentTreeNode.children.find(n => {
                        return n.fileItemId === fileModelTreeEvent.fileItem.getFileItemUniqueId()
                    });

                    if (nodeToDelete != null) {
                        let idxOfNodeToDelete: number = parentTreeNode.children.indexOf(nodeToDelete);
                        parentTreeNode.children.splice(idxOfNodeToDelete, 1);

                        this.addCountToContainerNode(parentTreeNode);

                        if (parentTreeNode.children.length === 0) {
                            this.removeItemFromSelectedNodes(parentTreeNode);

                        }

                    }

                } else {
                    // error?
                }
            } // if-else -if on extractor category type

        } else {
            // error condition: invalid event
        } // there i sno file mode node for tree event
    }

    placeNodeInTree(fileModelTreeEvent: FileModelTreeEvent) {

        if (fileModelTreeEvent.fileModelNode != null && fileModelTreeEvent.fileItem != null) {

            if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.LEAF) {

                let gobiiTreeLeafNodeTobeMutated: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());

                if (gobiiTreeLeafNodeTobeMutated != null) {


                    gobiiTreeLeafNodeTobeMutated.fileItemId = fileModelTreeEvent.fileItem.getFileItemUniqueId();
                    gobiiTreeLeafNodeTobeMutated.entityType = fileModelTreeEvent.fileItem.getEntityType();
                    gobiiTreeLeafNodeTobeMutated.entitySubType = fileModelTreeEvent.fileItem.getEntitySubType();
                    gobiiTreeLeafNodeTobeMutated.cvFilterType = fileModelTreeEvent.fileItem.getCvFilterType();

                    this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, gobiiTreeLeafNodeTobeMutated, fileModelTreeEvent.fileItem);
                    this.addIconsToNode(fileModelTreeEvent.fileModelNode, gobiiTreeLeafNodeTobeMutated, false);
                    gobiiTreeLeafNodeTobeMutated.required = fileModelTreeEvent.fileModelNode.getRequired();
                    if (this.selectedGobiiNodes.indexOf(gobiiTreeLeafNodeTobeMutated) === -1) {
                        this.selectedGobiiNodes.push(gobiiTreeLeafNodeTobeMutated);
                    }

                    if (gobiiTreeLeafNodeTobeMutated.parent !== null) {
                        if (this.selectedGobiiNodes.indexOf(gobiiTreeLeafNodeTobeMutated.parent) === -1) {
                            this.selectedGobiiNodes.push(gobiiTreeLeafNodeTobeMutated.parent);
                        }
                    }


                } else {

                    let message: string = "Error placing file item in the status tree: there is no gobii tree leaf node for model node "
                        + Labels.instance().treeExtractorTypeLabels[fileModelTreeEvent.fileModelNode.getItemType()];

                    if (fileModelTreeEvent.fileItem && fileModelTreeEvent.fileItem.getItemName()) {
                        message += " for fileItem of name " + fileModelTreeEvent.fileItem.getItemName();
                    }

                    this.handleAddStatusMessage(new HeaderStatusMessage(
                        message,
                        null,
                        null
                    ));

                } // if-else we found an existing node for the LEAF node's file item


            } else if (fileModelTreeEvent.fileModelNode.getCategoryType() === ExtractorCategoryType.CONTAINER) {

                // there should not be a file item associated with the model because it's a container -- the file items are just for the children
                let parentTreeNode: GobiiTreeNode = this.findTreeNodebyModelNodeId(this.gobiiTreeNodes, fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId());
                if (parentTreeNode != null) {

                    let existingFileModelItem: GobiiFileItem = fileModelTreeEvent
                        .fileModelNode
                        .getFileItems()
                        .find(item => {
                            return item.getFileItemUniqueId() === fileModelTreeEvent.fileItem.getFileItemUniqueId()
                        });

                    if (existingFileModelItem !== null) {


                        let existingGobiiTreeNodeChild: GobiiTreeNode = this.findTreeNodebyFileItemUniqueId(this.gobiiTreeNodes, existingFileModelItem.getFileItemUniqueId());

                        if (existingGobiiTreeNodeChild === null) {

                            let newGobiiTreeNode: GobiiTreeNode =
                                new GobiiTreeNode(parentTreeNode,
                                    fileModelTreeEvent.fileModelNode.getFileModelNodeUniqueId(),
                                    fileModelTreeEvent.fileItem.getFileItemUniqueId(),
                                    fileModelTreeEvent.fileModelNode.getRequired());
                            newGobiiTreeNode.entityType = fileModelTreeEvent.fileItem.getEntityType();
                            this.addIconsToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, false);
                            this.addEntityNameToNode(fileModelTreeEvent.fileModelNode, newGobiiTreeNode, fileModelTreeEvent.fileItem);
                            parentTreeNode.children.push(newGobiiTreeNode);
                            parentTreeNode.expanded = true;
                            parentTreeNode.required = false; //make it clickable
                            newGobiiTreeNode.parent = parentTreeNode;
                            this.selectedGobiiNodes.push(newGobiiTreeNode);

                            this.addCountToContainerNode(parentTreeNode);

                            if (parentTreeNode.children.length >= this.containerCollapseThreshold) {
                                parentTreeNode.expanded = false;

                            } else {
                                parentTreeNode.expanded = true;
                            }

                            if (this.selectedGobiiNodes.indexOf(parentTreeNode) < 0) {
                                this.selectedGobiiNodes.push(parentTreeNode);
                            }


                        } else {
                            // modify existing existingGobiiTreeNodeChild
                        } // if-else there already exists a corresponding tree node

                    } else {
                        // error condition
                    } // if else we found an existing file item


                } else {
                    // error condition
                } // if-else we found a tree node to serve as parent for the container's item tree nodes


            } // if-else -if on extractor category type


        } else {
            // error condition: invalid event
        } // there i sno file mode node for tree event

    } // place node in tree


    private treeIsInitialized: boolean = false;

    setUpRequredItems(gobiiExtractorFilterType: GobiiExtractFilterType) {

        this.treeIsInitialized = false;

        this.gobiiTreeNodes = [];

        let fileModelNodes: FileModelNode[] = []
        this._fileModelTreeService.getFileModel(gobiiExtractorFilterType).subscribe(
            f => {
                fileModelNodes = f;
            }
        );

        fileModelNodes.forEach(
            currentFirstLevelFileModelNode => {

                let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(null, currentFirstLevelFileModelNode);
                if (currentTreeNode != null) {
                    this.gobiiTreeNodes.push(currentTreeNode);
                }
            }
        );


        this.treeIsInitialized = true;

        this._fileModelTreeService.put(GobiiFileItem
            .build(this.gobiiExtractFilterType, ProcessType.NOTIFY)
            .setExtractorItemType(ExtractorItemType.STATUS_DISPLAY_TREE_READY)).subscribe(
            null,
            headerResponse => {
                this.handleAddStatusMessage(headerResponse)
            }
        );

    }

    makeTreeNodeFromTemplate(parentNode: GobiiTreeNode,
                             fileModelNode: FileModelNode): GobiiTreeNode {

        let returnVal: GobiiTreeNode = null;


        if (fileModelNode.getItemType() === ExtractorItemType.ENTITY) {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
            returnVal.entityType = fileModelNode.getEntityType();
            returnVal.label = fileModelNode.getEntityName();

            //.MODEL_CONTAINER

        } else if (fileModelNode.getItemType() == ExtractorItemType.EXPORT_FORMAT) {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
            returnVal.label = fileModelNode.getCategoryName();
        } else if (fileModelNode.getItemType() == ExtractorItemType.SAMPLE_LIST_ITEM) {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
            returnVal.label = fileModelNode.getCategoryName();
        } else if (fileModelNode.getItemType() == ExtractorItemType.MARKER_FILE) {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
            returnVal.label = fileModelNode.getCategoryName();
        } else if (fileModelNode.getItemType() == ExtractorItemType.JOB_ID) {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());
            returnVal.label = fileModelNode.getCategoryName();
        } else {

            returnVal = new GobiiTreeNode(parentNode, fileModelNode.getFileModelNodeUniqueId(), null, fileModelNode.getRequired());

            if (fileModelNode.getEntityType() != null
                && fileModelNode.getEntityType() != EntityType.UNKNOWN) {
                returnVal.entityType = fileModelNode.getEntityType();
            }

            returnVal.label = fileModelNode.getCategoryName();
        }


        if (null != returnVal) {

            let debug: string = "debug";
            this.addIconsToNode(fileModelNode, returnVal, true);

            returnVal.expanded = true;
            fileModelNode.getChildren().forEach(
                stt => {

                    let currentTreeNode: GobiiTreeNode = this.makeTreeNodeFromTemplate(returnVal, stt);
                    if (null != currentTreeNode) {
                        returnVal.children.push(currentTreeNode);
                    }
                }
            ); // iterate child model node
        } else {
            this.handleAddStatusMessage(new
                HeaderStatusMessage("Unable to make tree node for file model of type " + Labels.instance().treeExtractorTypeLabels[fileModelNode.getItemType()], null, null));
        }// if we created a tree node

        return returnVal;
    }


// ********************************************************************************
// ********************* CHECKBOX (GOBII-SPECIFIC)  NODE DATA STRUCTURES AND EVENTS


    gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;

    onItemChecked: EventEmitter < GobiiFileItem > = new EventEmitter();
    onItemSelected: EventEmitter < GobiiFileItem > = new EventEmitter();


    ngOnChanges(changes: {
        [propName: string
            ]: SimpleChange
    }) {

        if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {

            let itemChangedEvent: GobiiFileItem = changes['fileItemEventChange'].currentValue;


//            this.placeNodeInModel(itemChangedEvent);
            //this.treeNodes.push(treeNode);

            //this.placeNodeInModel(treeNode);

            // this.treeNodes.push(treeNode);
            //


            // if (this.itemChangedEvent) {
            //     let itemToChange:FileItem =
            //         this.fileItemEvents.filter(e => {
            //             return e.id == changes['fileItemEventChange'].currentValue.id;
            //         })[0];
            //
            //     //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
            //     if (itemToChange) {
            //         itemToChange.processType = changes['fileItemEventChange'].currentValue.processType;
            //         itemToChange.checked = changes['fileItemEventChange'].currentValue.checked;
            //     }
            // }
        } else if (changes['gobiiExtractFilterTypeEvent']
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != null )
            && ( changes['gobiiExtractFilterTypeEvent'].currentValue != undefined )) {

            if (changes['gobiiExtractFilterTypeEvent'].currentValue !== changes['gobiiExtractFilterTypeEvent'].previousValue) {
                this.gobiiExtractFilterType = changes['gobiiExtractFilterTypeEvent'].currentValue;
                this.setUpRequredItems(this.gobiiExtractFilterType);

            }

            // this.setList(changes['nameIdList'].currentValue);

        }
    }


    makeDemoTreeNodes() {

        this.demoTreeNodes = [
            {
                "label": "Documents",
                "data": "Documents Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [{

                    "label": "Work",
                    "data": "Work Folder",
                    "expandedIcon": "fa-folder-open",
                    "collapsedIcon": "fa-folder",
                    "children": [{

                        "label": "Expenses.doc",
                        "icon": "fa-file-word-o",
                        "data": "Expenses Document"
                    }, {"label": "Resume.doc", "icon": "fa-file-word-o", "data": "Resume Document"}]
                },
                    {

                        "label": "Home",
                        "data": "Home Folder",
                        "expandedIcon": "fa-folder-open",
                        "collapsedIcon": "fa-folder",
                        "children": [{

                            "label": "Invoices.txt",
                            "icon": "fa-file-word-o",
                            "data": "Invoices for this month"
                        }]
                    }]
            },
            {

                "label": "Pictures",
                "data": "Pictures Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [
                    {"label": "barcelona.jpg", "icon": "fa-file-image-o", "data": "Barcelona Photo"},
                    {"label": "logo.jpg", "icon": "fa-file-image-o", "data": "PrimeFaces Logo"},
                    {"label": "primeui.png", "icon": "fa-file-image-o", "data": "PrimeUI Logo"}]
            },
            {

                "label": "Movies",
                "data": "Movies Folder",
                "expandedIcon": "fa-folder-open",
                "collapsedIcon": "fa-folder",
                "children": [{

                    "label": "Al Pacino",
                    "data": "Pacino Movies",
                    "children": [{

                        "label": "Scarface",
                        "icon": "fa-file-video-o",
                        "data": "Scarface Movie"
                    }, {"label": "Serpico", "icon": "fa-file-video-o", "data": "Serpico Movie"}]
                },
                    {

                        "label": "Robert De Niro",
                        "data": "De Niro Movies",
                        "children": [{

                            "label": "Goodfellas",
                            "icon": "fa-file-video-o",
                            "data": "Goodfellas Movie"
                        }, {

                            "label": "Untouchables",
                            "icon": "fa-file-video-o",
                            "data": "Untouchables Movie"
                        }]
                    }]
            }
        ];


        this.selectedDemoNodes.push(this.demoTreeNodes[1].children[0])
        this.demoTreeNodes[1].partialSelected = true;
        this.demoTreeNodes[1].expanded = true;
    }
}
