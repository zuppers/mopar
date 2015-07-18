package io.mopar.rs2.tool;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author Hadyn Fitzgerald
 */
public class PackLandscapeKeys {

    public static void main(String... args) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("rs2/data/keys"))) {
            for(Path path : stream) {
                ByteBuffer buffer = ByteBuffer.allocate(18);

                File file = path.toFile();

                String name = file.getName();
                int region = Integer.parseInt(name.substring(0, name.indexOf('.')));

                buffer.putShort((short) region);

                BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
                for(int i = 0; i < 4; i++) {
                    int key = Integer.parseInt(reader.readLine());
                    buffer.putInt(key);
                }

                bos.write(buffer.array());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try(FileOutputStream os = new FileOutputStream("rs2/asset/game/landscape-keys.dat")) {
            os.write(bos.toByteArray());
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
