package org.opendaylight.controller.sal.restconf.impl.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.xml.parsers.*;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opendaylight.controller.md.sal.common.api.TransactionStatus;
import org.opendaylight.controller.sal.rest.impl.*;
import org.opendaylight.controller.sal.restconf.impl.*;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.data.api.*;
import org.opendaylight.yangtools.yang.data.impl.XmlTreeBuilder;
import org.opendaylight.yangtools.yang.model.api.*;
import org.opendaylight.yangtools.yang.model.parser.api.YangModelParser;
import org.opendaylight.yangtools.yang.parser.impl.YangParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;

public final class TestUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    private final static YangModelParser parser = new YangParserImpl();

    public static Set<Module> loadModules(String resourceDirectory) throws FileNotFoundException {
        final File testDir = new File(resourceDirectory);
        final String[] fileList = testDir.list();
        final List<File> testFiles = new ArrayList<File>();
        if (fileList == null) {
            throw new FileNotFoundException(resourceDirectory);
        }
        for (int i = 0; i < fileList.length; i++) {
            String fileName = fileList[i];
            if (new File(testDir, fileName).isDirectory() == false) {
                testFiles.add(new File(testDir, fileName));
            }
        }
        return parser.parseYangModels(testFiles);
    }

    public static SchemaContext loadSchemaContext(Set<Module> modules) {
        return parser.resolveSchemaContext(modules);
    }

    public static SchemaContext loadSchemaContext(String resourceDirectory) throws FileNotFoundException {
        return parser.resolveSchemaContext(loadModules(resourceDirectory));
    }

    public static Module findModule(Set<Module> modules, String moduleName) {
        Module result = null;
        for (Module module : modules) {
            if (module.getName().equals(moduleName)) {
                result = module;
                break;
            }
        }
        return result;
    }



    public static Document loadDocumentFrom(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            return docBuilder.parse(inputStream);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            logger.error("Error during loading Document from XML", e);
            return null;
        }
    }

    public static String getDocumentInPrintableForm(Document doc) {
        Preconditions.checkNotNull(doc);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(out, "UTF-8")));
            byte[] charData = out.toByteArray();
            return new String(charData, "UTF-8");
        } catch (IOException | TransformerException e) {
            String msg = "Error during transformation of Document into String";
            logger.error(msg, e);
            return msg;
        }

    }

    public static String convertCompositeNodeDataAndYangToJson(CompositeNode compositeNode, String yangPath,
            String outputPath, String searchedModuleName, String searchedDataSchemaName) {
        Set<Module> modules = resolveModules(yangPath);
        Module module = resolveModule(searchedModuleName, modules);
        DataSchemaNode dataSchemaNode = resolveDataSchemaNode(module, searchedDataSchemaName);

        normalizeCompositeNode(compositeNode, modules, dataSchemaNode, searchedModuleName + ":"
                + searchedDataSchemaName);

        try {
            return writeCompNodeWithSchemaContextToJson(compositeNode, modules, dataSchemaNode);
        } catch (WebApplicationException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static void normalizeCompositeNode(CompositeNode compositeNode, Set<Module> modules,
            DataSchemaNode dataSchemaNode, String schemaNodePath) {
        RestconfImpl restconf = RestconfImpl.getInstance();
        ControllerContext.getInstance().setSchemas(TestUtils.loadSchemaContext(modules));

        TestUtils.prepareMockForRestconfBeforeNormalization(modules, dataSchemaNode, restconf);
        restconf.createConfigurationData(schemaNodePath, compositeNode);
    }

    public static Module resolveModule(String searchedModuleName, Set<Module> modules) {
        assertNotNull("modules can't be null.", modules);
        Module module = null;
        if (searchedModuleName != null) {
            for (Module m : modules) {
                if (m.getName().equals(searchedModuleName)) {
                    module = m;
                    break;
                }
            }
        } else if (modules.size() == 1) {
            module = modules.iterator().next();
        }
        return module;
    }

    public static Set<Module> resolveModules(String yangPath) {
        Set<Module> modules = null;

        try {
            modules = TestUtils.loadModules(TestUtils.class.getResource(yangPath).getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return modules;
    }

    public static DataSchemaNode resolveDataSchemaNode(Module module, String searchedDataSchemaName) {
        assertNotNull("Module is missing", module);

        DataSchemaNode dataSchemaNode = null;
        if (searchedDataSchemaName != null) {
            for (DataSchemaNode dsn : module.getChildNodes()) {
                if (dsn.getQName().getLocalName().equals(searchedDataSchemaName)) {
                    dataSchemaNode = dsn;
                }
            }
        } else if (module.getChildNodes().size() == 1) {
            dataSchemaNode = module.getChildNodes().iterator().next();
        }
        return dataSchemaNode;
    }

    public static String writeCompNodeWithSchemaContextToJson(CompositeNode compositeNode, Set<Module> modules,
            DataSchemaNode dataSchemaNode) throws IOException, WebApplicationException {
        String jsonResult;

        assertNotNull(dataSchemaNode);
        assertNotNull("Composite node can't be null", compositeNode);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();

        ControllerContext.getInstance().setSchemas(loadSchemaContext(modules));

        StructuredDataToJsonProvider structuredDataToJsonProvider = StructuredDataToJsonProvider.INSTANCE;
        structuredDataToJsonProvider.writeTo(new StructuredData(compositeNode, dataSchemaNode), null, null, null, null,
                null, byteArrayOS);

        jsonResult = byteArrayOS.toString();

        return jsonResult;
    }

    public static CompositeNode loadCompositeNode(String xmlDataPath) {
        InputStream xmlStream = TestUtils.class.getResourceAsStream(xmlDataPath);
        CompositeNode compositeNode = null;
        try {
            XmlReader xmlReader = new XmlReader();
            compositeNode = xmlReader.read(xmlStream);

        } catch (UnsupportedFormatException | XMLStreamException e) {
            e.printStackTrace();
        }
        return compositeNode;
    }

    static void outputToFile(ByteArrayOutputStream outputStream, String outputDir) throws IOException {
        FileOutputStream fileOS = null;
        try {
            String path = TestUtils.class.getResource(outputDir).getPath();
            File outFile = new File(path + "/data.json");
            fileOS = new FileOutputStream(outFile);
            try {
                fileOS.write(outputStream.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileOS.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    static String readJsonFromFile(String path, boolean removeWhiteChars) {
        FileReader fileReader = getFileReader(path);

        StringBuilder strBuilder = new StringBuilder();
        char[] buffer = new char[1000];

        while (true) {
            int loadedCharNum;
            try {
                loadedCharNum = fileReader.read(buffer);
            } catch (IOException e) {
                break;
            }
            if (loadedCharNum == -1) {
                break;
            }
            strBuilder.append(buffer, 0, loadedCharNum);
        }
        try {
            fileReader.close();
        } catch (IOException e) {
            System.out.println("The file wasn't closed");
        }
        String rawStr = strBuilder.toString();
        if (removeWhiteChars) {
            rawStr = rawStr.replace("\n", "");
            rawStr = rawStr.replace("\r", "");
            rawStr = rawStr.replace("\t", "");
            rawStr = removeSpaces(rawStr);
        }

        return rawStr;
    }

    private static FileReader getFileReader(String path) {
        String fullPath = TestUtils.class.getResource(path).getPath();
        assertNotNull("Path to file can't be null.", fullPath);
        File file = new File(fullPath);
        assertNotNull("File can't be null", file);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull("File reader can't be null.", fileReader);
        return fileReader;
    }

    private static String removeSpaces(String rawStr) {
        StringBuilder strBuilder = new StringBuilder();
        int i = 0;
        int quoteCount = 0;
        while (i < rawStr.length()) {
            if (rawStr.substring(i, i + 1).equals("\"")) {
                quoteCount++;
            }

            if (!rawStr.substring(i, i + 1).equals(" ") || (quoteCount % 2 == 1)) {
                strBuilder.append(rawStr.charAt(i));
            }
            i++;
        }

        return strBuilder.toString();
    }

    public static QName buildQName(String name, String uri, String date) {
        try {
            URI u = new URI(uri);
            Date dt = null;
            if (date != null) {
                dt = Date.valueOf(date);
            }
            return new QName(u, dt, name);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public static QName buildQName(String name) {
        return buildQName(name, "", null);
    }

    public static void supplementNamespace(DataSchemaNode dataSchemaNode, CompositeNode compositeNode) {
        RestconfImpl restconf = RestconfImpl.getInstance();

        InstanceIdWithSchemaNode instIdAndSchema = new InstanceIdWithSchemaNode(mock(InstanceIdentifier.class),
                dataSchemaNode);

        ControllerContext controllerContext = mock(ControllerContext.class);
        BrokerFacade broker = mock(BrokerFacade.class);

        RpcResult<TransactionStatus> rpcResult = new DummyRpcResult.Builder<TransactionStatus>().result(
                TransactionStatus.COMMITED).build();
        Future<RpcResult<TransactionStatus>> future = DummyFuture.builder().rpcResult(rpcResult).build();
        when(controllerContext.toInstanceIdentifier(any(String.class))).thenReturn(instIdAndSchema);
        when(broker.commitConfigurationDataPut(any(InstanceIdentifier.class), any(CompositeNode.class))).thenReturn(
                future);

        restconf.setControllerContext(controllerContext);
        restconf.setBroker(broker);

        // method is called only because it contains call of method which
        // supplement namespaces to compositeNode
        restconf.createConfigurationData("something", compositeNode);
    }

    public static DataSchemaNode obtainSchemaFromYang(String yangFolder) throws FileNotFoundException {
        return obtainSchemaFromYang(yangFolder, null);
    }

    public static DataSchemaNode obtainSchemaFromYang(String yangFolder, String moduleName)
            throws FileNotFoundException {
        Set<Module> modules = null;
        modules = TestUtils.loadModules(TestUtils.class.getResource(yangFolder).getPath());

        if (modules == null) {
            return null;
        }
        if (modules.size() < 1) {
            return null;
        }

        Module moduleRes = null;
        if (modules.size() > 1) {
            if (moduleName == null) {
                return null;
            } else {
                for (Module module : modules) {
                    if (module.getName().equals(moduleName)) {
                        moduleRes = module;
                    }
                }
                if (moduleRes == null) {
                    return null;
                }
            }
        } else {
            moduleRes = modules.iterator().next();
        }

        if (moduleRes.getChildNodes() == null) {
            return null;
        }

        if (moduleRes.getChildNodes().size() != 1) {
            return null;
        }
        DataSchemaNode dataSchemaNode = moduleRes.getChildNodes().iterator().next();
        return dataSchemaNode;

    }

    public static void addDummyNamespaceToAllNodes(NodeWrapper<?> wrappedNode) throws URISyntaxException {
        wrappedNode.setNamespace(new URI(""));
        if (wrappedNode instanceof CompositeNodeWrapper) {
            for (NodeWrapper<?> childNodeWrapper : ((CompositeNodeWrapper) wrappedNode).getValues()) {
                addDummyNamespaceToAllNodes(childNodeWrapper);
            }
        }
    }

    public static void prepareMockForRestconfBeforeNormalization(Set<Module> modules, DataSchemaNode dataSchemaNode,
            RestconfImpl restconf) {
        ControllerContext instance = ControllerContext.getInstance();
        instance.setSchemas(TestUtils.loadSchemaContext(modules));
        restconf.setControllerContext(ControllerContext.getInstance());

        BrokerFacade mockedBrokerFacade = mock(BrokerFacade.class);
        when(mockedBrokerFacade.commitConfigurationDataPut(any(InstanceIdentifier.class), any(CompositeNode.class)))
                .thenReturn(
                        new DummyFuture.Builder().rpcResult(
                                new DummyRpcResult.Builder<TransactionStatus>().result(TransactionStatus.COMMITED)
                                        .build()).build());
        restconf.setBroker(mockedBrokerFacade);
    }
    
    static CompositeNode loadCompositeNodeWithXmlTreeBuilder(String xmlDataPath) {
        InputStream xmlStream = TestUtils.class.getResourceAsStream(xmlDataPath);
        CompositeNode compositeNode = null;
        try {
            compositeNode = TestUtils.loadCompositeNodeWithXmlTreeBuilder(xmlStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return compositeNode;
        
        
        
    }
    
    
    public static CompositeNode loadCompositeNodeWithXmlTreeBuilder(InputStream xmlInputStream) throws FileNotFoundException {
        if (xmlInputStream == null) {
            throw new IllegalArgumentException();
        }
        Node<?> dataTree;
        try {
            dataTree = XmlTreeBuilder.buildDataTree(xmlInputStream);
        } catch (XMLStreamException e) {
            logger.error("Error during building data tree from XML", e);
            return null;
        }
        if (dataTree == null) {
            logger.error("data tree is null");
            return null;
        }
        if (dataTree instanceof SimpleNode) {
            logger.error("RPC XML was resolved as SimpleNode");
            return null;
        }
        return (CompositeNode) dataTree;
    }        

}
