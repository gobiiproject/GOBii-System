System.register(["./link"], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var link_1;
    var Links;
    return {
        setters:[
            function (link_1_1) {
                link_1 = link_1_1;
            }],
        execute: function() {
            Links = (function () {
                function Links(exploreLinksPerDataItem, linksPerDataItem) {
                    this.exploreLinksPerDataItem = exploreLinksPerDataItem;
                    this.linksPerDataItem = linksPerDataItem;
                }
                Links.fromJSON = function (json) {
                    var exploreLinksPerDataItem = [];
                    json.exploreLinksPerDataItem.forEach(function (l) {
                        exploreLinksPerDataItem.push(link_1.Link.fromJSON(l));
                    });
                    var linksPerDataItem = [];
                    json.linksPerDataItem.forEach(function (l) {
                        linksPerDataItem.push(link_1.Link.fromJSON(l));
                    });
                    return new Links(exploreLinksPerDataItem, linksPerDataItem); // new
                }; // fromJson()
                return Links;
            }());
            exports_1("Links", Links);
        }
    }
});
//# sourceMappingURL=links.js.map