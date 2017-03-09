System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var Guid;
    return {
        setters: [],
        execute: function () {
            Guid = (function () {
                function Guid() {
                }
                Guid.generateUUID = function () {
                    var date = new Date().getTime();
                    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                        var random = (date + Math.random() * 16) % 16 | 0;
                        date = Math.floor(date / 16);
                        return (c == 'x' ? random : (random & 0x3 | 0x8)).toString(16);
                    });
                    return uuid;
                };
                ;
                return Guid;
            }());
            exports_1("Guid", Guid);
        }
    };
});
//# sourceMappingURL=guid.js.map