/*
 * Copyright 2016 htouhui.com All right reserved. This software is the
 * confidential and proprietary information of htouhui.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with haitouhui.com.
 */
package com.farm.core.thirdparty.oss;

/**
 */
public class OSSPathBuilder {

    private OSSClientFactory.Directory directory;
    private OSSClientFactory.DataType dataType;
    private String dataId;
    private String filename;

    public static OSSPathBuilder create() {
        OSSPathBuilder ossPathBuilder = new OSSPathBuilder();
        return ossPathBuilder;
    }

    public OSSPathBuilder withDirectory(OSSClientFactory.Directory directory) {
        this.directory = directory;
        return this;
    }

    public OSSPathBuilder withDataType(OSSClientFactory.DataType dataType, String dataId) {
        this.dataType = dataType;
        this.dataId = dataId;
        return this;
    }

    public OSSPathBuilder withFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public OSSPath build() {
        OSSPath ossPath = new OSSPath();
        ossPath.setDirectory(directory);
        ossPath.setDataType(dataType);
        ossPath.setDataId(dataId);
        ossPath.setFilename(filename);
        return ossPath;
    }
}
