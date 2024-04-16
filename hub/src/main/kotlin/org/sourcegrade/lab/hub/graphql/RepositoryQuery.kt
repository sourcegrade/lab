package org.sourcegrade.lab.hub.graphql

import org.sourcegrade.lab.hub.domain.DomainEntity
import org.sourcegrade.lab.hub.domain.Repository

class RepositoryQuery<E : DomainEntity>(val repository: Repository<E>) {

}
