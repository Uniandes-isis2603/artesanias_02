/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.uniandes.csw.artesanias.resources;

import co.edu.uniandes.csw.artesanias.dtos.ConferenciaDTO;
import co.edu.uniandes.csw.artesanias.dtos.detail.ConferenciaDetailDTO;
import co.edu.uniandes.csw.artesanias.ejbs.ConferenciaLogic;
import co.edu.uniandes.csw.artesanias.ejbs.FeriaLogic;
import co.edu.uniandes.csw.artesanias.entities.ConferenciaEntity;
import co.edu.uniandes.csw.artesanias.entities.FeriaEntity;
import co.edu.uniandes.csw.artesanias.exceptions.BusinessLogicException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author IVAN
 */
 



@Path( "ferias/{feriaId: \\d+}/conferencias" )
@Consumes( MediaType.APPLICATION_JSON )
@Produces( MediaType.APPLICATION_JSON )
public class ConferenciaResource
{
	@Inject
	private ConferenciaLogic conferenciaLogic;
        @Inject
        private FeriaLogic feriaLogic;
	
	@Context
	private HttpServletResponse response;
	
	private List<ConferenciaDTO> listEntity2DTO( List<ConferenciaEntity> entityList )
	{
		List<ConferenciaDTO> list = new ArrayList<>( );
		for( ConferenciaEntity entity : entityList )
		{
			list.add( new ConferenciaDTO( entity ) );
		}
		return list;
	}
	
	@GET
	public List<ConferenciaDTO> getConferenciasFromFeria( @PathParam("feriaId") Long feriaId) throws BusinessLogicException
	{
		if (feriaLogic.getFeria(feriaId)==null){
                    throw new WebApplicationException("No existe la feria", 404);
                }
		
                
            return listEntity2DTO( conferenciaLogic.getConferenciasFromFeria(feriaId ) );
	}
        
       
	
	@GET
	@Path( "{id: \\d+}" )
	public ConferenciaDTO getConferencia( @PathParam("feriaId")Long feriaId, @PathParam( "id" ) Long id ) throws BusinessLogicException
	{
           
            if(feriaLogic.getFeria(feriaId)==null){
                    throw new WebApplicationException("No existe la feria", 404);
            }
		return new ConferenciaDetailDTO( conferenciaLogic.getConferencia( id ,feriaId) );
	}
	
	@POST
	public ConferenciaDTO createConferencia( ConferenciaDTO dto ) throws BusinessLogicException
	{
		return new ConferenciaDTO( conferenciaLogic.createConferencia( dto.toEntity( ) ) );
	}
	
	@PUT
	@Path( "{id: \\d+}" )
	public ConferenciaDTO updateConferencia( @PathParam("feriaId")Long feriaId,
			@PathParam( "id" ) Long id, ConferenciaDTO dto ) throws BusinessLogicException
	{
            
             if(feriaLogic.getFeria(feriaId)==null){
                    throw new WebApplicationException("No existe la feria", 404);
            }
		
		ConferenciaEntity entity = dto.toEntity( );
		entity.setId( id );
		return new ConferenciaDTO( conferenciaLogic.updateConferencia( entity ) );
	}
	
	@DELETE
	@Path( "{id: \\d+}" )
	public void deleteSConferencia(@PathParam("feriaId")Long feriaId, @PathParam( "id" ) Long id ) throws BusinessLogicException
	{
             if(feriaLogic.getFeria(feriaId)==null){
                    throw new WebApplicationException("No existe la feria", 404);
            }
		
		conferenciaLogic.deleteConferencia( feriaId,id );
	}
}