package org.gobiiproject.gobiiclient.gobii.Helpers;

import org.gobiiproject.gobiimodel.headerlesscontainer.EntityPropertyDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/4/2016.
 */
public class EntityParamValues {

    private List<EntityPropertyDTO> properties = new ArrayList<>();

    public List<EntityPropertyDTO> getProperties() {
        return properties;
    }

    public void add(String name, String value) {
        properties.add(new EntityPropertyDTO(null, null, name, value));
    }

    public boolean compare(List<EntityPropertyDTO> propertiesToCompare) {

        boolean returnVal = true;

        for (int idx = 0; (idx < properties.size()) && returnVal; idx++) {

            EntityPropertyDTO currentProperty = properties.get(idx);

            List<EntityPropertyDTO> matchedProperties = propertiesToCompare
                    .stream()
                    .filter(m -> m.getPropertyName().equals(currentProperty.getPropertyName()))
                    .collect(Collectors.toList());

            if (matchedProperties.size() == 1) {
                EntityPropertyDTO matchedProperty = matchedProperties.get(0);
                returnVal = currentProperty.getPropertyValue().equals(matchedProperty.getPropertyValue());

            } else {
                returnVal = false;
            }

        }

        return returnVal;
    }

    public List<EntityPropertyDTO> getMissingEntityProperties(List<EntityPropertyDTO> propertiesToCompare) {

        List<EntityPropertyDTO> returnVal = new ArrayList<>();

        for (EntityPropertyDTO currentPropDto : this.properties) {

            List<EntityPropertyDTO> foundInDestination = propertiesToCompare
                    .stream()
                    .filter(d -> d.getPropertyName().equals(currentPropDto.getPropertyName())
                            && d.getPropertyValue().equals(currentPropDto.getPropertyValue()))
                    .collect(Collectors.toList());

            if (foundInDestination.size() != 1) {
                returnVal.add(currentPropDto);

            }

        }

        return returnVal;
    }
}
