/*
 * Copyright 2013 - Jeandeson O. Merelis
 */
package coffeepot.pos.tef.dial.response;

import coffeepot.pos.tef.dial.Constants;
import coffeepot.pos.tef.dial.TefDial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Jeandeson O. Merelis
 */
public class ResponseReader {

    public static ResponseMessage getResponseMessage(File file) throws FileNotFoundException, IOException {
        ResponseMessage result = null;
        StringBuilder sb = new StringBuilder();
        if (file.exists()) {
            FileReader in = new FileReader(file);
            try (BufferedReader reader = new BufferedReader(in)) {
                String line;
                line = reader.readLine();

                if (line != null) {
                    sb.append(line).append(Constants.CR_LF);

                    String field = line.substring(0, 7);
                    if (!field.equals(Constants.HEADER)) {
                        throw new IllegalArgumentException("O arquivo não possui um HEADER");
                    }

                    String value = line.substring(10);
                    TefDial.Operation oper = TefDial.Operation.valueOf(value);
                    result = new ResponseMessage(oper);
                }

                if (result == null) {
                    throw new IllegalStateException("Objeto de resposta é nulo");
                }

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(Constants.CR_LF);
                    String field = line.substring(0, 7);
                    String value = line.substring(10);
                    result.setValue(field, value);
                }
                result.originalFile = sb.toString();

            }
        }

        return result;

    }

    public static StatusResponse getStatusResponse(File file) throws FileNotFoundException, IOException {
        StatusResponse result = null;
        StringBuilder sb = new StringBuilder();
        if (file.exists()) {
            FileReader in = new FileReader(file);
            try (BufferedReader reader = new BufferedReader(in)) {
                String line;
                line = reader.readLine();

                if (line != null) {
                    sb.append(line).append(Constants.CR_LF);

                    String field = line.substring(0, 7);
                    if (!field.equals(Constants.HEADER)) {
                        throw new IllegalArgumentException("O arquivo não possui um HEADER");
                    }

                    String value = line.substring(10);
                    TefDial.Operation oper = TefDial.Operation.valueOf(value);
                    result = new StatusResponse();
                    result.operation = oper;
                }

                if (result == null) {
                    throw new IllegalStateException("Objeto de resposta é nulo");
                }

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append(Constants.CR_LF);
                    String field = line.substring(0, 7);
                    String value = line.substring(10);

                    if (field.equals("001-000")) {
                        result.identifier = Integer.valueOf(value);
                    } else if (!field.equals("999-999")) {
                        result.addField(field, value);
                    }
                }                
            }
        }

        return result;

    }
}
