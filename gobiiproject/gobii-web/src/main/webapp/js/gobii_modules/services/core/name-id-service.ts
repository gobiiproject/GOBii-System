import {Injectable} from "@angular/core";
import {NameId} from "../../model/name-id";
import {DtoRequestService} from "./dto-request.service";
import {EntityFilter} from "../../model/type-entity-filter";
import {CvFilterType, CvFilters} from "../../model/cv-filter-type";
import {EntityType, EntitySubType} from "../../model/type-entity";
import {NameIdRequestParams} from "../../model/name-id-request-params";
import {Observable} from 'rxjs/Observable';
import {DtoRequestItemNameIds} from "../app/dto-request-item-nameids";
/**
 * Created by Phil on 3/9/2017.
 */

@Injectable()
export class NameIdService {

    constructor(private _dtoRequestService: DtoRequestService<NameId[]>,) {

    } // ctor

    private getEntityFilterValue(nameIdRequestParams: NameIdRequestParams): string {

        let returnVal: string = null;

        if (nameIdRequestParams.getEntityType() === EntityType.Contacts) {
            if (nameIdRequestParams.getEntitySubType() === EntitySubType.CONTACT_PRINCIPLE_INVESTIGATOR) {
                returnVal = "PI";
            }
        } else if (nameIdRequestParams.getEntityType() === EntityType.CvTerms) {
            if (nameIdRequestParams.getCvFilterType() != null && nameIdRequestParams.getCvFilterType() != CvFilterType.UNKNOWN) {
                returnVal = CvFilters.get(CvFilterType.DATASET_TYPE);
            }
        }

        return returnVal;
    }

    public validateRequest(nameIdRequestParams: NameIdRequestParams): boolean {

        let foo:string = "bar";

        let returnVal: boolean = false;

        if (nameIdRequestParams.getEntityFilter() === EntityFilter.NONE) {

            nameIdRequestParams.setEntityFilterValue(null);
            returnVal = true;

        } else if (nameIdRequestParams.getEntityFilter() === EntityFilter.BYTYPEID) {

            //for filter BYTYPEID we must have a filter value specified by parent

            returnVal = (nameIdRequestParams.getEntityFilterValue() != null);

        } else if (nameIdRequestParams.getEntityFilter() === EntityFilter.BYTYPENAME) {

            //for filter BYTYPENAME we divine the typename algorityhmically for now
            let entityFilterValue: string = this.getEntityFilterValue(nameIdRequestParams);
            if (entityFilterValue) {
                nameIdRequestParams.setEntityFilterValue(entityFilterValue);
                returnVal = true;
            }
        }

        return returnVal;
    }


    public get(nameIdRequestParams: NameIdRequestParams): Observable < NameId[] > {

        return Observable.create(observer => {

                this._dtoRequestService.get(new DtoRequestItemNameIds(
                    nameIdRequestParams.getEntityType(),
                    nameIdRequestParams.getEntityFilter() === EntityFilter.NONE ? null : nameIdRequestParams.getEntityFilter(),
                    nameIdRequestParams.getEntityFilterValue()))
                    .subscribe(nameIds => {
                        let nameIdsToReturn: NameId[] = null;
                        if (nameIds && ( nameIds.length > 0 )) {
                            nameIdsToReturn = nameIds;
                        } else {
                            nameIdsToReturn = [new NameId("0", "<none>", nameIdRequestParams.getEntityType())];
                        }

                        observer.next(nameIdsToReturn);
                        observer.complete();
                    },
                    responseHeader => {

                        responseHeader.status.statusMessages.forEach(headerStatusMessage => {
                            observer.error(headerStatusMessage)
                        })
                    });


            }
        );
    }
}