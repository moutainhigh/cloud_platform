package cn.sensordb2.stcloud.server.gate;

import io.vertx.core.buffer.Buffer;

import java.util.Vector;

/**
 * Created by sensordb on 16/2/19.
 */
public class BufferSplitResult {
    Vector<Buffer> tokens;
    Buffer lastBuffer;

    public BufferSplitResult(Vector<Buffer> tokens, Buffer lastBuffer) {
        this.tokens = tokens;
        this.lastBuffer = lastBuffer;
    }

    public Vector<Buffer> getTokens() {
        return tokens;
    }

    public void setTokens(Vector<Buffer> tokens) {
        this.tokens = tokens;
    }

    public Buffer getLastBuffer() {
        return lastBuffer;
    }

    public void setLastBuffer(Buffer lastBuffer) {
        this.lastBuffer = lastBuffer;
    }
}
