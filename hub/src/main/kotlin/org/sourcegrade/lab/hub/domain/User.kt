package org.sourcegrade.lab.hub.domain

import org.jetbrains.exposed.sql.SizedIterable

interface User : DomainEntity {
    var username: String
    var displayname: String
    var email: String

    val courseMemberships: UserMembership.Accessor<Course>
    val submissionGroupsMemberships: UserMembership.Accessor<SubmissionGroup>
    suspend fun assignments(): SizedIterable<Assignment>
    suspend fun submissions(): SizedIterable<Submission>
}
