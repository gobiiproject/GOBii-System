import {CardinalityType} from "./file-model-node";


export class CardinalityExpression {

    constructor(cardinalityType: CardinalityType,
                isValid: Function,
                message: string) {

        this.cardinalityType = cardinalityType;
        this.isValid = isValid;
        this.message = message;

    }

    public cardinalityType: CardinalityType;
    public message: string;
    public isValid: Function;
}