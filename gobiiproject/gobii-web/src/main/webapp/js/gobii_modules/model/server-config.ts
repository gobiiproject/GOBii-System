export class ServerConfig {
    
    constructor(public crop:string,
                public domain:string,
                public contextRoot:string,
                public port:number) {
        
        this.crop = crop;
        this.domain = domain;
        this.port = port;
    }
}