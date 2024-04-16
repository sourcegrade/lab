package org.sourcegrade.lab.hub.graphql

import org.sourcegrade.lab.hub.domain.DomainEntity

class RepositoryQuery<E : DomainEntity>(val repository: Repository<E>) {

}
