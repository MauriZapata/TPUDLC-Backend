package controllers;

import Services.BuscadorService;
import domain.Documento;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/buscador")
public class BuscadorController {
    
    @Inject private BuscadorService _buscadorService;
    
    @GET
    @Path("/{query}")
    @Produces("application/json")
    public Response buscar(@PathParam("query") String query){
        
        List<Documento> documentos = _buscadorService.buscar(query);

        return Response.ok(documentos).build();
    }
    
}