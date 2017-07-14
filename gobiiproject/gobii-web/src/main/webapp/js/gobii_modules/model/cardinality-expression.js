System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var CardinalityExpression;
    return {
        setters: [],
        execute: function () {
            CardinalityExpression = (function () {
                function CardinalityExpression(cardinalityType, isValid, message) {
                    this.cardinalityType = cardinalityType;
                    this.isValid = isValid;
                    this.message = message;
                }
                return CardinalityExpression;
            }());
            exports_1("CardinalityExpression", CardinalityExpression);
        }
    };
});
//# sourceMappingURL=cardinality-expression.js.map