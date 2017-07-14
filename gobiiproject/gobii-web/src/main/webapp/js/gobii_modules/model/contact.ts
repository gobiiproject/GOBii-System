export class Contact {

    constructor(public contactId: number,
                public lastName: string,
                public firstName: string,
                public code: string,
                public email: string,
                //    public List<Integer> roles = new ArrayList<>(),
                //    public Integer createdBy,
                //    public Date createdDate,
                //    public Integer modifiedBy,
                //    public Date modifiedDate,
                public organizationId: number,
                public userName: string) {
    }
}