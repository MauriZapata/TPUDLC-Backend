
import domain.Documento;
import javax.inject.Inject;
import javax.ws.rs.*;

import javax.ws.rs.core.*;
import Services.IndexadorService;

@Path("/indexador")
public class IndexadorController {

    int cantIdx;
    @Inject
    private IndexadorService _indexadorService;

    @GET
    @Path("/")
    public Response comenzarIndexacion() {
        try {
            cantIdx = _indexadorService.indexar();
        } catch (Exception ex) {
            String mensaje = "Ha ocurrido un error al indexar los documentos: " + ex;
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(mensaje)
                    .build();
        }
        return Response.ok(cantIdx).build();
    }
}
