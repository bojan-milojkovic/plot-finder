package com.plot.finder.plot.entities;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VerticeListDeserializer extends JsonDeserializer<List<Vertice>> {

	@SuppressWarnings("deprecation")
	@Override
	public List<Vertice> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		ObjectNode objectNode = mapper.readTree(jsonParser);
		JsonNode nodeVertices = objectNode.get("vertices");
		
		if (null == nodeVertices                     // if no author node could be found
                || !nodeVertices.isArray()           // or author node is not an array
                || !nodeVertices.elements().hasNext())   // or author node doesn't contain any authors
            return null;
		
		CollectionType collectionType =
	            TypeFactory
	            .defaultInstance()
	            .constructCollectionType(List.class, Vertice.class);
		
		return mapper.reader(collectionType).readValue(nodeVertices);
	}

}
