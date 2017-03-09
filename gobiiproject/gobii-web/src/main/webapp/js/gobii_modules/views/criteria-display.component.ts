import {Component, OnInit, SimpleChange, EventEmitter} from "@angular/core";
import {FileItem} from "../model/file-item";
import {ProcessType} from "../model/type-process";
import {EntityType} from "../model/type-entity";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {CvFilterType} from "../model/cv-filter-type";


@Component({
    selector: 'criteria-display',
    inputs: ['dataSetFileItemEvents'],
    outputs: ['onItemUnChecked', 'onItemSelected'],
    template: `<form>
                    <div style="overflow:auto; height: 80px; border: 1px solid #336699; padding-left: 5px">
                        <div *ngFor="let dataSetFileItemEvent of dataSetFileItemEvents"
                                (click)=handleItemSelected($event)
                                (hover)=handleItemHover($event)>
                                <input  type="checkbox"
                                    (click)=handleItemUnChecked($event)
                                    value={{dataSetFileItemEvent.itemId}}
                                    name="{{dataSetFileItemEvent.itemName}}"
                                    checked>&nbsp;{{dataSetFileItemEvent.itemName}}
                        </div>
                    </div>
                </form>`

})

export class CriteriaDisplayComponent implements OnInit {


    // useg
    private dataSetFileItemEvents: FileItem[] = [];
    private onItemUnChecked: EventEmitter<FileItem> = new EventEmitter();
    private onItemSelected: EventEmitter<number> = new EventEmitter();

    constructor() {
    } // ctor


    ngOnInit(): any {
        return null;
    }

    // In this component, every item starts out checked; unchecking it removes it
    private handleItemUnChecked(arg) {
        let checkEvent: FileItem = FileItem.build(
            GobiiExtractFilterType.UNKNOWN,
            ProcessType.DELETE)
            .setEntityType(EntityType.DataSets)
            .setCvFilterType(CvFilterType.UKNOWN)
            .setItemId(arg.currentTarget.value)
            .setItemName(arg.currentTarget.name)
            .setChecked(false)
            .setRequired(false);

        let itemToRemove: FileItem =
            this.dataSetFileItemEvents
                .filter(e => {
                    return e.getItemId() === arg.currentTarget.value
                })[0];

        let indexOfItemToRemove: number = this.dataSetFileItemEvents.indexOf(itemToRemove);

        if (indexOfItemToRemove > -1) {
            this.dataSetFileItemEvents.splice(indexOfItemToRemove, 1);
        }

        this.onItemUnChecked.emit(checkEvent);
    }

    private previousSelectedItem: any;

    private handleItemSelected(arg) {

        let selectedDataSetId: number = Number(arg.currentTarget.children[0].value);
        if (this.previousSelectedItem) {
            this.previousSelectedItem.style = ""
        }
        arg.currentTarget.style = "background-color:#b3d9ff";
        this.previousSelectedItem = arg.currentTarget;
        this.onItemSelected.emit(selectedDataSetId);

    }


    ngOnChanges(changes: {[propName: string]: SimpleChange}) {
        this.dataSetFileItemEvents = changes['dataSetFileItemEvents'].currentValue;
    }

}
