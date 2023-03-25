package gr.aueb.cf.schoolapp.rest;


import gr.aueb.cf.schoolapp.dto.UserCredentialsDTO;
import gr.aueb.cf.schoolapp.model.User;
import gr.aueb.cf.schoolapp.service.IUserService;
import gr.aueb.cf.schoolapp.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.service.util.PasswordHashUtil;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/users")
public class UserRestController {

    @Inject
    private IUserService userService;

    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId") Long userId) {
        User user;
        try {
            user = userService.getUserById(userId);
            UserCredentialsDTO dto = new UserCredentialsDTO(user.getId(),
                    user.getUsername(), user.getPassword());
            return Response.status(Response.Status.OK).entity(dto).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("NOT FOUND").build();
        }
    }


    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeacher(UserCredentialsDTO dto, @Context UriInfo uriInfo) {
        try {
            dto.setPassword(PasswordHashUtil.hashPassword(dto.getPassword()));
            User user = userService.insertUser(dto);
            UserCredentialsDTO userCredentialsDTO = map(user);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            return Response.created(uriBuilder.path(Long.toString(userCredentialsDTO.getId())).build())
                    .entity(userCredentialsDTO).build();
        } catch (EntityAlreadyExistsException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("User already exists")
                    .build();
        }
    }

    @Path("/{userId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);
            userService.deleteUser(userId);
            UserCredentialsDTO userCredentialsDTO = map(user);
            return Response.status(Response.Status.OK).entity(userCredentialsDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User Not Found")
                    .build();
        }
    }

    @Path("/{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") Long userId, UserCredentialsDTO dto) {
        try {
            dto.setId(userId);
            User user = userService.updateUser(dto);
            UserCredentialsDTO userCredentialsDTO = map(user);
            return Response.status(Response.Status.OK).entity(userCredentialsDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response.status(Response.Status.NOT_FOUND).entity("User Not Found").build();
        }
    }

    private UserCredentialsDTO map(User user) {
        UserCredentialsDTO dto = new UserCredentialsDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
