
export interface DtoRequestItem<T> {
    getUrl():string;
    getRequestBody(): string;
    resultFromJson(json):T;
} 
