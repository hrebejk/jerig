/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.codeviation.strast.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.codeviation.commons.utils.StreamUtil;
import org.codeviation.strast.Store;
import org.codeviation.strast.Strast;

/** Resource for managing/querying the storage through the lowest (file) level.
 *
 * @author Petr Hrebejk
 */
public class ContentResource {

    private Store store;

    public ContentResource( Store store ) {
        this.store = store;
    }

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("{path:.*}")
    public Response getFile(@PathParam("path") String path) {

        InputStream is = store.getInputStream(Strast.path(path));

        if ( is == null ) {
            return Response.status(Status.NOT_FOUND).entity(path).build();
        }

        return Response.ok(is).type(MediaType.APPLICATION_JSON_TYPE).build();

    }

    @POST
    @Path("{path:.*}")
    public Response putFile(@PathParam("path") String path,
                            InputStream is ) throws IOException {

        OutputStream os = store.getOutputStream(Strast.path(path));

        StreamUtil.copy(is, os);
        os.close();

        return Response.created(uriInfo.getBaseUriBuilder().path(path).build()).build();

    }

    @DELETE
    @Path("{path:.*}")
    public Response deleteFile(@PathParam("path") String path) {
        store.delete(Strast.path(path));
        return Response.ok().build(); // XXX
    }
   
}
