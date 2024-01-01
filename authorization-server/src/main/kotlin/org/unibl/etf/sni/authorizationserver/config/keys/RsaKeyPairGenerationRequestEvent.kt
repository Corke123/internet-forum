package org.unibl.etf.sni.authorizationserver.config.keys

import org.springframework.context.ApplicationEvent
import java.time.Instant

class RsaKeyPairGenerationRequestEvent(instant: Instant) : ApplicationEvent(instant) {

    override fun getSource() = super.getSource() as Instant

}
