package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ResourcesFileLoader implements Loader {
  private final String fileName;

  public ResourcesFileLoader(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public List<Measurement> load() throws IOException {
    File inputFile;
    try {
      var resource = ResourcesFileLoader.class.getClassLoader().getResource(fileName);
      inputFile = new File(resource.toURI());
    } catch (URISyntaxException | NullPointerException ex){
      throw new FileProcessException("Cannot find/access file <%s>".formatted(fileName));
    }

    ObjectMapper mapper = new ObjectMapper();

    return mapper.readValue(inputFile, new TypeReference<List<Measurement>>() {
    });
  }
}
