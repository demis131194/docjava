package ru.javaops.docjava.xml.xpath;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.javaops.docjava.TestData.inputFile;

class XPathUtilTest {
    @Test
    void evaluateBoolean() throws Exception {
        Boolean result = (Boolean) XPathUtil.evaluate(inputFile,
                "count(/UsersWithMeals/Users/User/Meals/Meal/text())<100");
        assertEquals(true, result);
    }

    @Test
    void evaluateNumber() throws Exception {
        Double result = (Double) XPathUtil.evaluate(inputFile,
                "count(/UsersWithMeals/Users/User/Meals/Meal/text())");
        assertEquals(9.0, result);
    }

    @Test
    void evaluateNode() throws Exception {
        Node node = (Node) XPathUtil.evaluate(inputFile,
                "/UsersWithMeals/Users/User[2]/Meals/Meal[1]/text()");
        assertEquals("Админ ланч", node.getNodeValue());
    }

    @Test
    void evaluateNodeList() throws Exception {
        List<? extends Node> nodes = (List<? extends Node>) XPathUtil.evaluate(inputFile,
                "/UsersWithMeals/Users/User[2]/Meals/Meal/text()");
        assertEquals(2, nodes.size());
        assertEquals("Админ ланч", nodes.get(0).getNodeValue());
        assertEquals("Админ ужин", nodes.get(1).getNodeValue());
    }
}