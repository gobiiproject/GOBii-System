export class Guid {

    public static generateUUID(): string {

        let date: number = new Date().getTime();

        let uuid:string = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let random:number = (date + Math.random() * 16) % 16 | 0;
            date = Math.floor(date / 16);
            return (c == 'x' ? random : (random & 0x3 | 0x8)).toString(16);
        });

        return uuid;
    };
}   