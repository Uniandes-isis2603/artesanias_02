/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.artesanias.resources;


import co.edu.uniandes.csw.artesanias.dtos.FeriaDTO;
import co.edu.uniandes.csw.artesanias.dtos.OrganizadorDTO;
import co.edu.uniandes.csw.artesanias.dtos.detail.OrganizadorDetailDTO;
import co.edu.uniandes.csw.artesanias.ejbs.OrganizadorLogic;
import co.edu.uniandes.csw.artesanias.entities.OrganizadorEntity;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author IVAN
 */
@Path("/ferias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrganizadorResource {
    
    @Inject private OrganizadorLogic organizadorLogic;
    @Context private HttpServletResponse response;
    @QueryParam("page") private Integer page;
    @QueryParam("limit") private Integer maxRecords;
    
      private List<OrganizadorDTO> listEntity2DTO(List<OrganizadorEntity> entityList){
        List<OrganizadorDTO> list = new ArrayList<>();
        for (OrganizadorEntity entity : entityList) {
            list.add(new OrganizadorDetailDTO(entity));
        }
        return list;
    }
    
    @GET
    public List<OrganizadorDTO> getOrganizadores() {
        
        return listEntity2DTO(organizadorLogic.getOrganizadores());
    }
    
     @GET
    @Path("{id: \\d+}")
    public OrganizadorDetailDTO getOrganizador(@PathParam("id") Long id) {
        return new OrganizadorDetailDTO(organizadorLogic.getOrganizador(id));
    }
    
    @GET
    @Path(("{id: \\d+}/ferias"))
    public List<FeriaDTO> getFeriasDeOrganizador(@PathParam( "id" ) Long id ){
        
        OrganizadorDetailDTO x=new OrganizadorDetailDTO(organizadorLogic.getOrganizador(id));
        return x.getFerias();
    }
    
    
    @POST
    public OrganizadorDTO createOrganizador(OrganizadorDTO dto) {
        return new OrganizadorDTO(organizadorLogic.createOrganizador(dto.toEntity()));
    }
    
    
     @PUT
    @Path("{id: \\d+}")
    public OrganizadorDetailDTO updateOrganizador(@PathParam("id") Long id, OrganizadorDetailDTO dto) {
        OrganizadorEntity entity = dto.toEntity();
        entity.setId(id);
        return new OrganizadorDetailDTO(organizadorLogic.updateOrganizador(entity));
    }
    
     @DELETE
    @Path("{id: \\d+}")
    public void deleteOrganizador(@PathParam("id") Long id) {
        organizadorLogic.deleteOrganizador(id);
    }
}
