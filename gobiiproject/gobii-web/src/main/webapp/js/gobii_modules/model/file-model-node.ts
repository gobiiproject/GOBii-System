import {EntityType, EntitySubType} from "./type-entity";
import {CvFilterType} from "./cv-filter-type";
import {GobiiTreeNode} from "./GobiiTreeNode";
import {FileItem} from "./file-item";
import {Guid} from "./guid";

export enum ExtractorItemType {
    UNKNOWN,
    CATEGORY,
    ENTITY,
    MARKER_LIST,
    SAMPLE_LIST,
    EXPORT_FORMAT,
    CROP_TYPE}

export enum ExtractorCategoryType {MODEL_CONTAINER, ENTITY_CONTAINER, CATEGORY_CONTAINER, LEAF }
export enum CardinalityType {ZERO_OR_ONE, ZERO_OR_MORE, ONE_ONLY, ONE_OR_MORE, MORE_THAN_ONE}


export class FileModelNode {

    constructor(itemType: ExtractorItemType, parent: FileModelNode) {
        this._itemType = itemType;
        this._parent = parent;
    }

    private _parent: FileModelNode = null;
    private _children: FileModelNode[] = [];
    private _alternatePeerTypes: EntityType[] = [];
    private _cardinality: CardinalityType = CardinalityType.ZERO_OR_MORE;
    private _itemType: ExtractorItemType = ExtractorItemType.ENTITY;
    private _categoryType: ExtractorCategoryType = ExtractorCategoryType.LEAF;
    private _categoryName: string;
    private _entityType: EntityType = EntityType.UNKNOWN;
    private _entitySubType: EntitySubType = EntitySubType.UNKNOWN;
    private _entityName: string;
    private _cvFilterType: CvFilterType = CvFilterType.UKNOWN;
    private _fileItems: FileItem[] = [];
    private _fileModelNodeUniqueId = Guid.generateUUID();


    public static build(itemType: ExtractorItemType, parent: FileModelNode): FileModelNode {
        return new FileModelNode(itemType, parent);
    }


    getAlternatePeerTypes(): EntityType[] {
        return this._alternatePeerTypes;
    }

    setAlternatePeerTypes(value: EntityType[]): FileModelNode {
        this._alternatePeerTypes = value;
        return this;
    }

    getCardinality(): CardinalityType {
        return this._cardinality;
    }

    setCardinality(value: CardinalityType): FileModelNode {
        this._cardinality = value;
        return this;
    }


    getParent(): FileModelNode {
        return this._parent;
    }

    getChildren(): FileModelNode[] {
        return this._children;
    }

    addChild(child: FileModelNode): FileModelNode {
        this._children.push(child);
        return this;
    }

    getItemType(): ExtractorItemType {
        return this._itemType;
    }

    setItemType(value: ExtractorItemType): FileModelNode {
        this._itemType = value;
        return this;
    }

    getCategoryType(): ExtractorCategoryType {
        return this._categoryType;
    }

    setCategoryType(value: ExtractorCategoryType): FileModelNode {
        this._categoryType = value;
        return this;
    }

    getCategoryName(): string {
        return this._categoryName;
    }

    setCategoryName(value: string): FileModelNode {
        this._categoryName = value;
        return this;
    }

    getEntityType(): EntityType {
        return this._entityType;
    }

    setEntityType(value: EntityType): FileModelNode {
        this._entityType = value;
        return this;
    }

    getEntitySubType(): EntitySubType {
        return this._entitySubType;
    }

    setEntitySubType(value: EntitySubType): FileModelNode {
        this._entitySubType = value;
        return this;
    }

    getEntityName(): string {
        return this._entityName;
    }

    setEntityName(value: string): FileModelNode {
        this._entityName = value;
        return this;
    }

    getCvFilterType(): CvFilterType {
        return this._cvFilterType;
    }

    setCvFilterType(value: CvFilterType): FileModelNode {
        this._cvFilterType = value;
        return this;
    }


    getFileItems(): FileItem[] {
        return this._fileItems;
    }

    setChildFileItems(value: FileItem[]): FileModelNode {
        this._fileItems = value;
        return this;
    }

    getFileModelNodeUniqueId(): string {
        return this._fileModelNodeUniqueId;
    }
}