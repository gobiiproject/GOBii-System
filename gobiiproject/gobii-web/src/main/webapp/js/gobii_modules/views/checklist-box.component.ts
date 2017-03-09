import {Component, OnInit, OnChanges, SimpleChange, EventEmitter, Input} from "@angular/core";
import {NameId} from "../model/name-id";
import {DtoRequestService} from "../services/core/dto-request.service";
import {ProcessType} from "../model/type-process";
import {FileItem} from "../model/file-item";
import {EntityType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";


@Component({
    selector: 'checklist-box',
    inputs: ['fileItemEventChange', 'nameIdList'],
    outputs: ['onItemSelected', 'onItemChecked', 'onAddMessage'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let fileItemEvent of fileItemEvents" 
                            (click)=handleItemSelected($event) 
                            (hover)=handleItemHover($event)>
                            <input  type="checkbox" 
                                (click)=handleItemChecked($event)
                                [checked]="fileItemEvent.getChecked()"
                                value={{fileItemEvent.getItemId()}} 
                                name="{{fileItemEvent.getItemName()}}">&nbsp;{{fileItemEvent.getItemName()}}
                        </div>            
                    </div>
                </form>` // end template

})


export class CheckListBoxComponent implements OnInit,OnChanges {

    constructor(private _dtoRequestServiceNameId: DtoRequestService<NameId[]>) {

    } // ctor

    // useg
    private nameIdList: NameId[];
    private fileItemEvents: FileItem[] = [];
    private entityType: EntityType = EntityType.UNKNOWN
    private onItemChecked: EventEmitter<FileItem> = new EventEmitter();
    private onItemSelected: EventEmitter<FileItem> = new EventEmitter();
    private onAddMessage: EventEmitter<string> = new EventEmitter();

    private handleItemChecked(arg) {

        let itemToChange: FileItem =
            this.fileItemEvents.filter(e => {
                return e.getItemId() === arg.currentTarget.value;
            })[0];

        //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
        itemToChange.setProcessType(arg.currentTarget.checked ? ProcessType.CREATE : ProcessType.DELETE);
        itemToChange.setChecked(arg.currentTarget.checked);
        this.onItemChecked.emit(itemToChange);

    } // handleItemChecked()

    private handleAddMessage(arg) {
        this.onAddMessage.emit(arg);
    }

    private previousSelectedItem: any;

    private handleItemSelected(arg) {

        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;

        let fileItemEvent: FileItem = FileItem.build(
            GobiiExtractFilterType.UNKNOWN,
            ProcessType.READ)
            .setEntityType(this.entityType)
            .setCvFilterType(CvFilterType.UKNOWN)
            .setItemId(arg.currentTarget.children[0].value)
            .setItemName(arg.currentTarget.children[0].name)
            .setChecked(false)
            .setRequired(false);

        this.onItemSelected.emit(fileItemEvent);

    }

    public setList(nameIdList: NameId[]): void {

        // we can get this event whenver the item is clicked, not necessarily when the checkbox
        let scope$ = this;

        scope$.nameIdList = nameIdList;

        if (scope$.nameIdList && ( scope$.nameIdList.length > 0 )) {

            scope$.entityType = scope$.nameIdList[0].entityType;

            scope$.fileItemEvents = [];
            scope$.nameIdList.forEach(n => {
                scope$.fileItemEvents.push(FileItem.build(
                    GobiiExtractFilterType.UNKNOWN,
                    ProcessType.CREATE)
                    .setEntityType(scope$.entityType)
                    .setCvFilterType(CvFilterType.UKNOWN)
                    .setItemId(n.id)
                    .setItemName(n.name)
                    .setChecked(false)
                    .setRequired(false)
                );
            });

        } else {
            scope$.nameIdList = [new NameId("0", "<none>", this.entityType)];
        }


    } // setList()


    ngOnInit(): any {

    }

    private eventedFileItem: FileItem;

    ngOnChanges(changes: {[propName: string]: SimpleChange}) {

        let bar: string = "foo";

        if (changes['fileItemEventChange'] && changes['fileItemEventChange'].currentValue) {

            this.eventedFileItem = changes['fileItemEventChange'].currentValue;

            if (this.eventedFileItem) {
                let itemToChange: FileItem =
                    this.fileItemEvents.filter(e => {
                        return e.getFileItemUniqueId() == this.eventedFileItem.getFileItemUniqueId();
                    })[0];

                //let indexOfItemToChange:number = this.fileItemEvents.indexOf(arg.currentTarget.name);
                if (itemToChange) {
                    itemToChange.setProcessType(this.eventedFileItem.getProcessType());
                    itemToChange.setChecked(this.eventedFileItem.getChecked());
                }
            }
        } else if (changes['nameIdList'] && changes['nameIdList'].currentValue) {

            this.setList(changes['nameIdList'].currentValue);

        } else if (changes['entityType'] && changes['entityType'].currentValue) {

            let enrityTypeString: string = changes['entityType'].currentValue;
            this.entityType = EntityType[enrityTypeString];
        }
    }
}
