
/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Urls implements Serializable {

    @SerializedName("raw")
    @Expose
    private String raw;
    @SerializedName("full")
    @Expose
    private String full;
    @SerializedName("regular")
    @Expose
    private String regular;
    @SerializedName("small")
    @Expose
    private String small;
    @SerializedName("thumb")
    @Expose
    private String thumb;

    /**
     * 
     * @return
     *     The raw
     */
    public String getRaw() {
        return raw;
    }

    /**
     * 
     * @param raw
     *     The raw
     */
    public void setRaw(String raw) {
        this.raw = raw;
    }

    /**
     * 
     * @return
     *     The full
     */
    public String getFull() {
        return full;
    }

    /**
     * 
     * @param full
     *     The full
     */
    public void setFull(String full) {
        this.full = full;
    }

    /**
     * 
     * @return
     *     The regular
     */
    public String getRegular() {
        return regular;
    }

    /**
     * 
     * @param regular
     *     The regular
     */
    public void setRegular(String regular) {
        this.regular = regular;
    }

    /**
     * 
     * @return
     *     The small
     */
    public String getSmall() {
        return small;
    }

    /**
     * 
     * @param small
     *     The small
     */
    public void setSmall(String small) {
        this.small = small;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }


}
