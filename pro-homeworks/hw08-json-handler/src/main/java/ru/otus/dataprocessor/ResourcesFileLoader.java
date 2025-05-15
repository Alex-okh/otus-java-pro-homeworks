package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
    this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        File inputFile = new File("src/test/resources/" + fileName);
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(inputFile, new TypeReference<List<Measurement>>(){});
    }
}
