package org.sourcegrade.lab.hub.domain

interface SubmissionGroupCategory : DomainEntity {
    val course: Course
    var name: String
    var minSize: Int
    var maxSize: Int
}
