use rumqtt::{MqttClient, QoS, Error};
use std::sync::{Arc, RwLock};
use std::io::{Write, Result};

pub struct MqttLogger {
    buffer: Vec<u8>,
    client: RwLock<Option<RwLock<MqttClient>>>,
}

impl Write for MqttLogger {
    fn write(&mut self, buf: &[u8]) -> Result<usize> {
        self.buffer.extend_from_slice(buf);
        Ok(buf.len())
    }

    fn flush(&mut self) -> Result<()> {
        use std::mem;
        if let Some(ref client) = *self.client.write().unwrap() {
            client.write().unwrap().publish("/Vision/log", QoS::Level2, mem::replace(&mut self.buffer, Vec::new()))
            .map_err(|e| {
                use std::io::{Error as IoError, ErrorKind};
                match e {
                    Error::Io(io) => io,
                    _ => IoError::new(ErrorKind::Other, e),
                }
            })
        } else {Ok(())}
    }
}