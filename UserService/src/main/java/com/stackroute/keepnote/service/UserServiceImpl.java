package com.stackroute.keepnote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the UserRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	
	UserRepository userrepo;
	@Autowired
	void UserServiceImpl(UserRepository repo)
	{
		this.userrepo=repo;
	}

	/*
	 * This method should be used to save a new user.Call the corresponding method
	 * of Respository interface.
	 */

	public User registerUser(User user) throws UserAlreadyExistsException {
		
			User u=userrepo.insert(user);
			if(u!=null)
			{
				return u;
			}
			else
				throw new UserAlreadyExistsException("user already exist");
		
	}

	/*
	 * This method should be used to update a existing user.Call the corresponding
	 * method of Respository interface.
	 */

	public User updateUser(String userId,User user) throws UserNotFoundException {

		//if(user.getUserId()==userId)
		if(userrepo.findById(userId)!=null)
		{
			userrepo.save(user);
			return user;
		}
		else
			throw new UserNotFoundException("user not found");
	}

	/*
	 * This method should be used to delete an existing user. Call the corresponding
	 * method of Respository interface.
	 */

	public boolean deleteUser(String userId) throws UserNotFoundException {

		if(userrepo.findById(userId)!=null)
			 {
				userrepo.deleteById(userId);
				return true;
			 }
		else
			{
			throw new UserNotFoundException("user not found");
			}
		
	}

	/*
	 * This method should be used to get a user by userId.Call the corresponding
	 * method of Respository interface.
	 */

	public User getUserById(String userId) throws UserNotFoundException {

		if(userrepo.findById(userId)!=null)
		 {
			return userrepo.findById(userId).get();
		 }
	else
		{
		throw new UserNotFoundException("user not found");
		}
	}

}
