package io.mopar.rs2.msg.file;

import io.mopar.core.msg.Message;

/**
 * @author Hadyn Fitzgerald
 */
public class FileRequestMessage extends Message {

    private int volumeId;
    private int fileId;
    boolean priority;

    public FileRequestMessage(int volumeId, int fileId, boolean priority) {
        this.volumeId = volumeId;
        this.fileId = fileId;
        this.priority = priority;
    }

    public int getVolumeId() {
        return volumeId;
    }

    public int getFileId() {
        return fileId;
    }

    public boolean isPriority() {
        return priority;
    }
}
