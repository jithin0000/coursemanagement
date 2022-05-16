package com.jithin.coursemanagement.controller

import com.jithin.coursemanagement.converters.StudentRegisterToProfileConverter
import com.jithin.coursemanagement.dto.ErrorMessage
import com.jithin.coursemanagement.dto.StudentRegisterRequest
import com.jithin.coursemanagement.exceptions.RoleInvalidIdException
import com.jithin.coursemanagement.exceptions.UserAlreadyExistException
import com.jithin.coursemanagement.models.CUser
import com.jithin.coursemanagement.services.RoleService
import com.jithin.coursemanagement.services.UserProfileService
import com.jithin.coursemanagement.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/profile")
class UserProfileController {

    @Autowired
    private lateinit var profileService: UserProfileService

    @Autowired
    private lateinit var mStudentRegisterToProfileConverter: StudentRegisterToProfileConverter

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var passWordEncoder:BCryptPasswordEncoder

    @PostMapping("/create")
    fun profileCreate(@Valid @RequestBody body: StudentRegisterRequest , principal: Principal): ResponseEntity<Any> {

        val createdBy = userService.findUserByEmail(principal.name)
        if(createdBy.isEmpty)
            return ResponseEntity.status(401).body(ErrorMessage("unauthenticated user"))

        val existingUser =userService.findUserByEmail(body.email)
        if(existingUser.isPresent)
            return ResponseEntity.badRequest().body(ErrorMessage("Already user exist with this mail"))
        val role = roleService.findByName("ROLE_"+body.roleName)
        if(role.isEmpty)
            return ResponseEntity.badRequest().body(ErrorMessage("no role with this name exist ${body.roleName}"))

        val user = CUser().apply {
           email = body.email
            password = passWordEncoder.encode(body.password)
            roles = setOf(role.get())
        }
        val cUser = userService.create(user)

        val userProfile = mStudentRegisterToProfileConverter.convert(body)
        userProfile!!.user=cUser
        userProfile.createdBy=createdBy.get()
        val profileCreated = profileService.create(userProfile)

        return ResponseEntity.status(201).body(profileCreated)

    }

}