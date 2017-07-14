package org.gobiiproject.gobiidao.resultset.core.listquery;

/**
 * Created by Phil on 10/25/2016.
 */
public enum ListSqlId {
    QUERY_ID_DATASET_ALL("select * from dataset order by lower(name)"),
    QUERY_ID_CONTACT_ALL("select * from contact order by lower(lastname),lower(firstname)"),
    QUERY_ID_ORGANIZATION_ALL("select * from organization order by lower(name)"),
    QUERY_ID_PLATFORM_ALL("select * from platform order by lower(name)"),
    QUERY_ID_PROJECT_ALL("select * from project order by lower(name)"),
    QUERY_ID_EXPERIMENT("select e.*\n" +
            "from experiment e\n" +
            "order by lower(e.name)"),
    QUERY_ID_MARKER_ALL("select m.marker_id,\n" +
            "p.platform_id,\n" +
            "m.variant_id, \n" +
            "m.name \"marker_name\", \n" +
            "m.code, \n" +
            "m.ref, \n" +
            "m.alts, \n" +
            "m.sequence, \n" +
            "m.reference_id, \n" +
            "m.strand_id, \n" +
            "m.status, \n" +
            "p.name \"platform_name\"\n" +
            "from marker m\n" +
            "join platform p on (m.platform_id=p.platform_id)\n" +
            "order by lower(m.name)"),
    QUERY_ID_PROTOCOL_ALL("select * from protocol order by lower(name)");

    private String sql;

    ListSqlId(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
