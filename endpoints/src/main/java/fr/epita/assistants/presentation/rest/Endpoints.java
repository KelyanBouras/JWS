package fr.epita.assistants.presentation.rest;


import fr.epita.assistants.presentation.rest.request.ReverseRequest;
import fr.epita.assistants.presentation.rest.response.HelloResponse;
import fr.epita.assistants.presentation.rest.response.ReverseResponse;
import okhttp3.ResponseBody;

import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Endpoints {

    @GET
    @Path("hello/{name}")
    public Response hello(@PathParam("name") String name)
    {
        return Response.ok((new HelloResponse(name))).build();
    }

    @Path("reverse")
    @POST
    public Response reverse(ReverseRequest request)
    {
        if (request == null || request.content == null || request.content.equals(""))
        {
            return Response.status(400).build();
        }
        Response.ResponseBuilder b = Response.ok(new ReverseResponse(request.content));
        return b.build();
    }
}
