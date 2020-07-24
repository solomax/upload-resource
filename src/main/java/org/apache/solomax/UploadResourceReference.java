package org.apache.solomax;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.fileupload.FileItem;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.AbstractResource.WriteCallback;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.request.resource.AbstractResource.ResourceResponse;
import org.apache.wicket.request.resource.IResource.Attributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.openjson.JSONObject;

public class UploadResourceReference extends ResourceReference {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UploadResourceReference.class);
	private static enum Status {
		SUCCESS
		, ERROR
	}

	public UploadResourceReference() {
		super(UploadResourceReference.class, "test-upload");
	}

	@Override
	public IResource getResource() {
		return new AbstractResource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected ResourceResponse newResourceResponse(Attributes attributes) {
				final ResourceResponse response = new ResourceResponse();
				final ServletWebRequest webRequest = (ServletWebRequest) attributes.getRequest();
				try {
					MultipartServletWebRequest multiPartRequest = webRequest.newMultipartWebRequest(Bytes.bytes(10_000_000), "ignored");
					Map<String, List<FileItem>> fileMap = multiPartRequest.getFiles();
					log.info("Got Multipart request!");
					log.info("   - param count: {}", multiPartRequest.getPostParameters().getParameterNames().size());
					log.info("   - different files count: {}", fileMap.size());
					for (String name : multiPartRequest.getPostParameters().getParameterNames()) {
						log.info("   - {} -> {}", name, multiPartRequest.getPostParameters().getParameterValue(name));
					}
					for (Entry<String, List<FileItem>> e : fileMap.entrySet()) {
						log.info("   - {} -> {}", e.getKey(), e.getValue().size());
					}
					prepareResponse(response, Status.SUCCESS, "Hooray");
				} catch (Exception e) {
					log.error("An error occurred while uploading a file", e);
					prepareResponse(response, Status.ERROR, e.getMessage());
				}
				return response;
			}
		};
	}

	private static void prepareResponse(ResourceResponse response, Status status, String msg) {
		response.setContentType("application/json");
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(Attributes attributes) throws IOException {
				attributes.getResponse().write(new JSONObject()
						.put("status", status)
						.put("message", msg)
						.toString());
			}
		});
	}
}
