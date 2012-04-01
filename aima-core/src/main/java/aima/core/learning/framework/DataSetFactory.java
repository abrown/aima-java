package aima.core.learning.framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import aima.core.learning.data.DataResource;

/**
 * Provides methods for importing and creating example sets
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DataSetFactory {

    /**
     * Returns a set of examples from a given CSV file and a sample Example;
     * CSV file should be located in the "aima/core/learning/data" directory.
     * @param filename
     * @param sample
     * @param separator
     * @return
     * @throws Exception 
     */
    public DataSet fromFile(String filename, String separator, Example sample) throws Exception {
        DataSet ds = new DataSet();
        InputStreamReader stream = new InputStreamReader(DataResource.class.getResourceAsStream(filename));
        BufferedReader reader = new BufferedReader(stream);
        String line;
        // loop through lines
        while ((line = reader.readLine()) != null) {
            ds.add(fromLine(line, separator, sample));
        }
        // return
        return ds;
    }

    /**
     * Returns an Example from a CSV line
     * @param line
     * @param separator
     * @param sample
     * @return 
     */
    public static Example fromLine(String line, String separator, Example sample) {
        // set attributes
        String[] parts = line.split(separator);
        Example new_example = new Example();
        for(int i = 0; i < sample.inputAttributes.size(); i++){
            if( i < parts.length ){
                Attribute new_attribute = (Attribute) sample.inputAttributes.get(i);
                new_attribute.setValue(parts[i]);
                new_example.add(new_attribute);
            }
        }
        // set output value; uses reflection to match the generic outputValue type
        try{
            String last_field = parts[parts.length-1];
            Class type = sample.outputValue.getClass();
            new_example.outputValue = type.getDeclaredConstructor(String.class).newInstance(last_field);
        }
        catch( Exception e ){ 
            new_example.outputValue = null;
        }
        // return
        return new_example;
    }

//    public static DataSet getRestaurantDataSet() throws Exception {
//        DataSetSpecification spec = createRestaurantDataSetSpec();
//        return new DataSetFactory().fromFile("restaurant", "\\s+", spec);
//    }
//
//    public static DataSetSpecification createRestaurantDataSetSpec() {
//        DataSetSpecification dss = new DataSetSpecification();
//        dss.defineStringAttribute("alternate", Util.yesno());
//        dss.defineStringAttribute("bar", Util.yesno());
//        dss.defineStringAttribute("fri/sat", Util.yesno());
//        dss.defineStringAttribute("hungry", Util.yesno());
//        dss.defineStringAttribute("patrons", new String[]{"None", "Some",
//                    "Full"});
//        dss.defineStringAttribute("price", new String[]{"$", "$$", "$$$"});
//        dss.defineStringAttribute("raining", Util.yesno());
//        dss.defineStringAttribute("reservation", Util.yesno());
//        dss.defineStringAttribute("type", new String[]{"French", "Italian",
//                    "Thai", "Burger"});
//        dss.defineStringAttribute("wait_estimate", new String[]{"0-10",
//                    "10-30", "30-60", ">60"});
//        dss.defineStringAttribute("will_wait", Util.yesno());
//        // last attribute is the target attribute unless the target is
//        // explicitly reset with dss.setTarget(name)
//
//        return dss;
//    }
//
//    public static DataSet getIrisDataSet() throws Exception {
//        DataSetSpecification spec = createIrisDataSetSpec();
//        return new DataSetFactory().fromFile("iris", ",", spec);
//    }
//
//    public static DataSetSpecification createIrisDataSetSpec() {
//        DataSetSpecification dss = new DataSetSpecification();
//        dss.defineNumericAttribute("sepal_length");
//        dss.defineNumericAttribute("sepal_width");
//        dss.defineNumericAttribute("petal_length");
//        dss.defineNumericAttribute("petal_width");
//        dss.defineStringAttribute("plant_category", new String[]{"setosa",
//                    "versicolor", "virginica"});
//        return dss;
//    }
}
