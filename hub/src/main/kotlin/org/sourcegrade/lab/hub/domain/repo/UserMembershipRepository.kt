package org.sourcegrade.lab.hub.domain.repo

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.SizedIterable
import org.sourcegrade.lab.hub.domain.MutableRepository
import org.sourcegrade.lab.hub.domain.Repository
import org.sourcegrade.lab.hub.domain.Term
import org.sourcegrade.lab.hub.domain.TermScoped
import org.sourcegrade.lab.hub.domain.UserMembership

interface UserMembershipRepository<T : TermScoped> : Repository<UserMembership<T>> {
    suspend fun find(
        status: UserMembership.Status = UserMembership.Status.CURRENT,
        term: Term.Matcher = Term.Matcher.Current,
        now: Instant = Clock.System.now(),
    ): SizedIterable<T>
}

interface MutableUserMembershipRepository<T : TermScoped> : UserMembershipRepository<T>,
    MutableRepository<UserMembership<T>, UserMembership.CreateDto<T>>
