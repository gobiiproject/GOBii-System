package org.gobiiproject.gobiimodel.dto.instructions.loader;

/**
 * Mostly unused VCF File parameters.
 * Created by Phil on 4/12/2016.
 */
public class VcfParameters {

    //As in vcfTools filter parameters
    private Float maf = null;
    private Float minQ = null;
    private Float minDp = null;
    private boolean removeIndels;

    //If true, convert to IUPAC before storing in HDF5. (Should always be true).
    private boolean   toIupac;

    public Float getMaf() {
        return maf;
    }

    public VcfParameters setMaf(Float maf) {
        this.maf = maf;
        return this;
    }

    public Float getMinQ() {
        return minQ;
    }

    public VcfParameters setMinQ(Float minQ) {
        this.minQ = minQ;
        return this;
    }

    public Float getMinDp() {
        return minDp;
    }

    public VcfParameters setMinDp(Float minDp) {
        this.minDp = minDp;
        return this;
    }

    public boolean isRemoveIndels() {
        return removeIndels;
    }

    public VcfParameters setRemoveIndels(boolean removeIndels) {
        this.removeIndels = removeIndels;
        return this;
    }

    public boolean isToIupac() {
        return toIupac;
    }

    public VcfParameters setToIupac(boolean toIupac) {
        this.toIupac = toIupac;
        return this;
    }
}
