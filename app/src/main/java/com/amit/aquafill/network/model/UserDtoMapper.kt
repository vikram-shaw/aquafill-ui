package com.amit.aquafill.network.model

import com.amit.aquafill.domain.model.User
import com.amit.aquafill.domain.util.DomainMapper

class UserDtoMapper: DomainMapper<UserDto, User> {
    override fun mapToDomainModel(model: UserDto): User {
        return User(
            email = model.email,
            password = model.password
        )
    }

    override fun mapFromDomainModel(domainModel: User): UserDto {
        return UserDto(
            email = domainModel.email,
            password = domainModel.password
        )
    }
}