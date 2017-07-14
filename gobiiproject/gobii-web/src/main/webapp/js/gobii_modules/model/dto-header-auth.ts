
export class DtoHeaderAuth {

    public constructor(public userName:string,
                       public password:string,
                       public token:string,
                       public gobiiCropType:string ) {

        this.userName = userName;
        this.password = password;
        this.token = token;
        this.gobiiCropType=gobiiCropType;
    }


    public getToken():string {
        return this.token;
    }
    
    public getGobiiCropType():string {
        return this.gobiiCropType;
    }


    public static fromJSON(json:JSON):DtoHeaderAuth {

        return new DtoHeaderAuth(
            json['userName'],
            json['password'],
            json['token'],
            json['gobiiCropType']
        );

    }


}
