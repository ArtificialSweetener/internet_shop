package service;

import java.util.Optional;

import models.UserSalt;

public interface UserSaltService extends GenericService<UserSalt> {
	public byte[] generateRandomSaltBytes();

	public Optional<UserSalt> getSaltByUserId(Long userId);
}
