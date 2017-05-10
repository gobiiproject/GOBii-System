import {Component, OnInit, SimpleChange, EventEmitter, ViewChild} from "@angular/core";
import {
    FileSelectDirective,
    FileDropDirective,
    FileUploader, FileUploaderOptions, Headers, FileItem
} from 'ng2-file-upload';
import {AuthenticationService} from "../services/core/authentication.service";
import {HeaderNames} from "../model/header-names";
import {Header} from "../model/payload/header";
import {HeaderStatusMessage} from "../model/dto-header-status-message";
import {FileName} from "../model/file_name";
import {FileModelTreeService} from "../services/core/file-model-tree-service";
import {GobiiFileItem} from "../model/gobii-file-item";
import {GobiiExtractFilterType} from "../model/type-extractor-filter";
import {ProcessType} from "../model/type-process";
import {ExtractorItemType} from "../model/file-model-node";
import {GobiiUIEventOrigin} from "../model/type-event-origin";

const URL = 'gobii/v1/uploadfile?gobiiExtractFilterType=BY_MARKER';

@Component({
    selector: 'uploader',
    inputs: ['gobiiExtractFilterType'],
    outputs: ['onUploaderError', 'onClickBrowse'],
    template: `<style>
    .my-drop-zone { border: dotted 3px lightgray; }
    .nv-file-over { border: dotted 3px red; } /* Default class applied to drop zones on over */
    .another-file-over-class { border: dotted 3px green; }

    html, body { height: 100%; }
</style>

<div class="container">

    <div class="row">

        <div class="col-md-3">

            <!-- DROP ZONES AND MULTI-FILE SELECT, UNUSED FOR NOW ====================== 
            <h3>Select files</h3>

            <div ng2FileDrop
                 [ngClass]="{'nv-file-over': hasBaseDropZoneOver}"
                 (fileOver)="fileOverBase($event)"
                 [uploader]="uploader"
                 class="well my-drop-zone">
                Base drop zone
            </div>

            <div ng2FileDrop
                 [ngClass]="{'another-file-over-class': hasAnotherDropZoneOver}"
                 (fileOver)="fileOverAnother($event)"
                 [uploader]="uploader"
                 class="well my-drop-zone">
                Another drop zone
            </div>

            Multiple
            <input type="file" ng2FileSelect [uploader]="uploader" multiple /><br/>
            ================================================================================ -->

            
            <input #selectedFile 
                type="file" 
                ng2FileSelect 
                [uploader]="uploader"
                 [disabled]="uploadComplete"
                (click)="handleClickBrowse($event)"/>
            <!--  IF YOU REINSTATE THE QUEUES BELOW THIS BUTTON WILL BE SUPERFLUOUS -->
            <BR>
            <button type="button" class="btn btn-success"
                        (click)="uploader.uploadAll()" 
                        [disabled]="!uploader.getNotUploadedItems().length">
                    Upload
            </button>
        </div>

        <div class="col-md-9" style="margin-bottom: 40px">


            <!-- UPLOAD QUEUE UNUSED FOR NOW =========================================================
            <h3>Upload queue</h3>
            <p>Queue length: {{ uploader?.queue?.length }}</p>

            <table class="table">
                <thead>
                <tr>
                    <th width="50%">Name</th>
                    <th>Size</th>
                    <th>Progress</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr *ngFor="let item of uploader.queue">
                    <td><strong>{{ item?.file?.name }}</strong></td>
                    <td *ngIf="uploader.isHTML5" nowrap>{{ item?.file?.size/1024/1024 | number:'.2' }} MB</td>
                    <td *ngIf="uploader.isHTML5">
                        <div class="progress" style="margin-bottom: 0;">
                            <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': item.progress + '%' }"></div>
                        </div>
                    </td>
                    <td class="text-center">
                        <span *ngIf="item.isSuccess"><i class="glyphicon glyphicon-ok"></i></span>
                        <span *ngIf="item.isCancel"><i class="glyphicon glyphicon-ban-circle"></i></span>
                        <span *ngIf="item.isError"><i class="glyphicon glyphicon-remove"></i></span>
                    </td>
                    <td nowrap>
                        <button type="button" class="btn btn-success btn-xs"
                                (click)="item.upload()" [disabled]="item.isReady || item.isUploading || item.isSuccess">
                            <span class="glyphicon glyphicon-upload"></span> Upload
                        </button>
                        <button type="button" class="btn btn-warning btn-xs"
                                (click)="item.cancel()" [disabled]="!item.isUploading">
                            <span class="glyphicon glyphicon-ban-circle"></span> Cancel
                        </button>
                        <button type="button" class="btn btn-danger btn-xs"
                                (click)="item.remove()">
                            <span class="glyphicon glyphicon-trash"></span> Remove
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>

            <div>
                <div>
                    Queue progress:
                    <div class="progress" style="">
                        <div class="progress-bar" role="progressbar" [ngStyle]="{ 'width': uploader.progress + '%' }"></div>
                    </div>
                </div>
                <button type="button" class="btn btn-success btn-s"
                        (click)="uploader.uploadAll()" [disabled]="!uploader.getNotUploadedItems().length">
                    <span class="glyphicon glyphicon-upload"></span> Upload all
                </button>
                <button type="button" class="btn btn-warning btn-s"
                        (click)="uploader.cancelAll()" [disabled]="!uploader.isUploading">
                    <span class="glyphicon glyphicon-ban-circle"></span> Cancel all
                </button>
                <button type="button" class="btn btn-danger btn-s"
                        (click)="uploader.clearQueue()" [disabled]="!uploader.queue.length">
                    <span class="glyphicon glyphicon-trash"></span> Remove all
                </button>
            </div>
            == UPLOAD QUEUE UNUSED FOR NOW ========================================================= -->


        </div>

    </div>

</div>`

})

export class UploaderComponent implements OnInit {

    private onUploaderError: EventEmitter<HeaderStatusMessage> = new EventEmitter();
    private gobiiExtractFilterType: GobiiExtractFilterType = GobiiExtractFilterType.UNKNOWN;
    private uploadComplete = false;

    constructor(private _authenticationService: AuthenticationService,
                private _fileModelTreeService: FileModelTreeService) {

        let fileUploaderOptions: FileUploaderOptions = {}
        fileUploaderOptions.url = URL;
        fileUploaderOptions.headers = [];
        fileUploaderOptions.removeAfterUpload = true;

        let authHeader: Headers = {name: '', value: ''};
        authHeader.name = HeaderNames.headerToken;

        let token: string = _authenticationService.getToken();

        if (token) {
            authHeader.value = token;

            fileUploaderOptions.headers.push(authHeader);

            this.uploader = new FileUploader(fileUploaderOptions);

            this.uploader.onBeforeUploadItem = (fileItem: FileItem) => {

                this._fileModelTreeService.getFileItems(this.gobiiExtractFilterType).subscribe(
                    fileItems => {
                        let fileItemJobId: GobiiFileItem = fileItems.find(item => {
                            return item.getExtractorItemType() === ExtractorItemType.JOB_ID
                        });

                        let jobId: string = fileItemJobId.getItemId();
                        fileItem.file.name = FileName.makeFileNameFromJobId(this.gobiiExtractFilterType, jobId);
                    });
            }

            this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {

                if (status == 200) {
                    let listItemType: ExtractorItemType =
                        this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER ?
                            ExtractorItemType.MARKER_FILE : ExtractorItemType.SAMPLE_FILE;

                    _fileModelTreeService.put(GobiiFileItem
                        .build(this.gobiiExtractFilterType, ProcessType.CREATE)
                        .setExtractorItemType(listItemType)
                        .setItemId(item.file.name)
                        .setItemName(item.file.name))
                        .subscribe(fme => {
                                this.uploadComplete = true;
                            },
                            headerStatusMessage => {
                                this.onUploaderError.emit(new HeaderStatusMessage(headerStatusMessage, null, null));
                            });
                } else {

                    this.onUploaderError.emit(new HeaderStatusMessage(response, null, null));

                }

            };
        } else {
            this.onUploaderError.emit(new HeaderStatusMessage("Unauthenticated", null, null));
        }

    } // ctor


    public uploader: FileUploader;

    public hasBaseDropZoneOver: boolean = false;
    public hasAnotherDropZoneOver: boolean = false;

    public fileOverBase(e: any): void {
        this.hasBaseDropZoneOver = e;
    }

    public fileOverAnother(e: any): void {
        this.hasAnotherDropZoneOver = e;
    }

    @ViewChild('selectedFile') selectedFile: any;

    clearSelectedFile() {
        this.selectedFile.nativeElement.value = '';
    }

    private onClickBrowse: EventEmitter<any> = new EventEmitter();

    private handleClickBrowse(event: any) {

        this.onClickBrowse.emit(event);

    }

    ngOnInit(): any {

        this._fileModelTreeService
            .fileItemNotifications()
            .subscribe(eventedFileItem => {
                    if (eventedFileItem.getProcessType() === ProcessType.DELETE) {
                        let currentItemType: ExtractorItemType = ExtractorItemType.UNKNOWN;
                        if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_SAMPLE) {
                            currentItemType = ExtractorItemType.SAMPLE_FILE;
                        } else if (this.gobiiExtractFilterType === GobiiExtractFilterType.BY_MARKER) {
                            currentItemType = ExtractorItemType.MARKER_FILE;
                        }

                        if ((eventedFileItem.getGobiiEventOrigin() === GobiiUIEventOrigin.CRITERIA_TREE)
                            && (eventedFileItem.getExtractorItemType() === currentItemType )) {
                            this.clearSelectedFile();
                            this.uploadComplete = false;
                        }
                    }
                },
                responseHeader => {
                    this.onUploaderError.emit(responseHeader);
                });
    }
}
