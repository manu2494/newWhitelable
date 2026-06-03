package com.merkle.xp.whitelabel.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Download;
import com.day.cq.wcm.api.designer.Style;
import com.merkle.xp.whitelabel.core.bean.DownloadListBean;
import com.merkle.xp.whitelabel.core.testcontext.AppAemContext;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
public class DownloadListModelImplTest {

    private static final String PAGE = "/content/core-framework/us/TEST/milk-calculator-test-page/contentfragmentdetailtest";
    private static final String COMPONENT = PAGE + "/jcr:content/root/container";
    private static final String TEST_ROOT_PAGE = "/content/downloads";
    private static final String TEST_ROOT_PAGE_GRID = "/jcr:content/root/responsivegrid";
    private static final String CF= "/content/dam/content-fragment/sn/global/dnc/learning-asset/en/theFeedCovid19DuringBreastfeedingPregnancyAndInfancy";
    private static final String DOWNLOAD_1 = TEST_ROOT_PAGE + TEST_ROOT_PAGE_GRID + "/download-1";
    private static final String ASSETRESOURCE = "/content/dam/sn/hcp/global/dnc/Items3.png";
    private static final String DOWNLOAD_2 = "/content/sn/global/dnc/global/en/learning-assets/details/jcr:content/root/container/container";
    MockSlingHttpServletRequest request;

    @Mock
    private Resource mockResource;

    public ResourceResolverFactory resolverFactory;
    
    private String[] elementNames;
    
    @Mock
    private ResourceResolver resourceResolver;
    
    @Mock
    private Style style;
    
    @Mock
    private Link link;
    
    private static final String CONTEXT_PATH = "/core";

    private final AemContext context = AppAemContext.newAemContext();

    public static DownloadListModelImpl downloadListModelImpl = new DownloadListModelImpl();
    
    private static final String TITLE = "Download";
    private static final String DESCRIPTION = "Description";
    private static final String DAM_TITLE = "This is the title from the PDF.";
    private static final String DAM_DESCRIPTION = "This is the description from the PDF.";
    private static final String PDF_FILESIZE_STRING = "147 KB";
    private static final String PDF_FILENAME = "Download_Test_PDF.pdf";
    private static final String PDF_FORMAT_STRING = "application/pdf";
    private static final String PDF_EXTENSION = "pdf";
    private SlingBindings slingBindings;
    
    private static final String PN_INLINE = "inline";
    
    private static final String PN_DISPLAY_FILENAME = "displayFilename";

    private static final String PN_HIDE_TITLE_LINK = "hideTitleLink";

    
    private static final String PN_ACTION_TEXT = "actionText";

    
    private static final String PN_TITLE_TYPE = "titleType";

    
    private static final String PN_DISPLAY_SIZE = "displaySize";

    
    private static final String PN_DISPLAY_FORMAT = "displayFormat";
    
    static MockedStatic<FilenameUtils> fileNameUtils;
    
    @BeforeAll
    static void before() {
    	fileNameUtils = Mockito.mockStatic(FilenameUtils.class);
    }
    @AfterAll
    static void after() {
    	fileNameUtils.close();
    }


    @BeforeEach
    public void setup() throws Exception {
    	context.load().json("/components/embed-model.json", COMPONENT);
    	context.load().json("/components/fileproperty.json", DOWNLOAD_1);
    	context.load().json("/components/assetfile.json", ASSETRESOURCE);
    	context.load().json("/components/downloadlist.json", DOWNLOAD_2);
    	context.load().json("/pages/CF.json", CF);
    	context.request().setAttribute("assetCfPath", CF);
    	elementNames = new String[] {"title"};
    	
    	context.addModelsForClasses(DownloadListModelImpl.class);
    	SlingBindings bindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
        bindings.put(SlingBindings.REQUEST, request);
        bindings.put("", style);
        bindings.put(SlingBindings.RESOURCE, mockResource);
        context.request().setAttribute(SlingBindings.class.getName(), bindings);
        lenient().when(style.get(Download.PN_DISPLAY_FORMAT, true)).thenReturn(true);
        lenient().when(style.get(Download.PN_TITLE_FROM_ASSET, true)).thenReturn(true);
        lenient().when(style.get(Download.PN_DISPLAY_SIZE, true)).thenReturn(true);
        lenient().when(style.get(Download.PN_DISPLAY_FILENAME,true)).thenReturn(true);
        lenient().when(style.get(Download.PN_HIDE_TITLE_LINK, false)).thenReturn(false);
        lenient().when(style.get(Download.PN_INLINE, true)).thenReturn(true);
        
        
    }
    private void setSlingBindings() {
		slingBindings = (SlingBindings) context.request().getAttribute(SlingBindings.class.getName());
		slingBindings.put(SlingBindings.REQUEST, request);
		slingBindings.put("currentStyle", style);
	    context.request().setAttribute(SlingBindings.class.getName(), slingBindings);
	    context.currentResource(context.resourceResolver().getResource(COMPONENT));
		downloadListModelImpl = context.request().adaptTo(DownloadListModelImpl.class);

	}

    @Test
    void testNavigationWithDependencies() {
    	context.currentResource(context.resourceResolver().getResource(COMPONENT));
    	DownloadListModelImpl model = context.request().adaptTo(DownloadListModelImpl.class);
    	model.getElementNames();
    	assertNotNull(model);
    	assertEquals("",model.getVariant());
    }
    
    @Test
    void testUrl() {
    	
        style = mock(Style.class);
        when(style.get(PN_TITLE_TYPE,String.class)).thenReturn("test");
        when(style.get(PN_DISPLAY_SIZE,true)).thenReturn(true);
        when(style.get(PN_DISPLAY_FORMAT,true)).thenReturn(true);
        when(style.get(PN_DISPLAY_FILENAME,true)).thenReturn(true);
        when(style.get(PN_HIDE_TITLE_LINK,false)).thenReturn(false);
        setSlingBindings();
        Resource resource = context.resourceResolver().getResource(COMPONENT);
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(context.resourceResolver(),
                context.bundleContext());
        request.setResource(resource);
        //DownloadListModelImpl model = context.request().adaptTo(DownloadListModelImpl.class);
       
        assertNotNull(downloadListModelImpl);

    }
    
    @Test
    void testFileData() {
        context.currentResource(context.resourceResolver().getResource(DOWNLOAD_1));
        mockResource = Mockito.mock(Resource.class);
        
        lenient().when(mockResource.getPath()).thenReturn("/content/dam/sn/hcp/global/dnc/Items3.png");
        DownloadListModelImpl model = context.request().adaptTo(DownloadListModelImpl.class);
        lenient().when(FilenameUtils.getExtension(model.getFilename())).thenReturn("PNG");
        
        List<DownloadListBean> beans = model.getFileData(mockResource);
        assertNotNull(model);
        assertEquals(1,beans.size());
        DownloadListBean downloadListBean = beans.get(0);
		assertNull(downloadListBean.getDescription());
        assertEquals("png",downloadListBean.getExtension());
        assertEquals("Items3.png",downloadListBean.getFileName());
        assertEquals("image/png",downloadListBean.getFormat());
        assertEquals(1676882652000L,downloadListBean.getLastModified());
        assertNotNull(downloadListBean.getLink());
        assertEquals("http://localhost/content/dam/sn/hcp/global/dnc/Items3.png.coredownload.png",downloadListBean.getLink().getExternalizedURL());
        assertEquals("/content/dam/sn/hcp/global/dnc/Items3.png.coredownload.png",downloadListBean.getLink().getMappedURL());
        assertEquals("/content/dam/sn/hcp/global/dnc/Items3.png.coredownload.png",downloadListBean.getLink().getURL());
        assertTrue(downloadListBean.getLink().isValid());
        assertEquals("103 KB",downloadListBean.getSize());
        assertNull(downloadListBean.getTitle());
    }
    
    @Test
    void testAssetDownload() {
        Resource componentRes=context.currentResource(context.resourceResolver().getResource(DOWNLOAD_2));
        mockResource = Mockito.mock(Resource.class);
        lenient().when(mockResource.getPath()).thenReturn("/content/dam/sn/hcp/global/dnc/Items3.png");
        DownloadListModelImpl model = context.request().adaptTo(DownloadListModelImpl.class);
        mockResource = Mockito.mock(Resource.class);
        lenient().when(mockResource.getPath()).thenReturn(ASSETRESOURCE);
        final List<String> elementArray = new ArrayList<String>();
		//Resource resource = context.resourceResolver().getResource(DYNAMIC_PAGE_CF);
        String element = "/content/dam/sn/hcp/global/dnc/Items3.png";
        elementArray.add(element);
		Iterator<String> itr = elementArray.iterator();
		lenient().when(mockResource.getPath()).thenReturn(element);
		Resource elementRes=context.currentResource(context.resourceResolver().getResource(element));
        context.currentResource(context.resourceResolver().getResource(ASSETRESOURCE));
        model.getDescription();
        model.getDownloadList();
        DownloadListBean bean = new DownloadListBean();
        model.getExtension();
        model.hideTitleLink();
        model.displayFilename();
        model.displayFormat();
        model.displaySize();
        model.getTitleType();
        model.getActionText();
        model.getTitle();
        model.displayFilename();
        model.getCFPath();
        model.getElementNames();
        model.getExportedType();
        model.getUrl();
        model.getFilename();
        model.getFormat();
        model.getSize();
        assertNotNull(model);
        
    }
}

