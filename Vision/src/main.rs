#![warn(clippy)]
extern crate cv;
extern crate rumqtt;
#[macro_use]
extern crate serde_derive;
extern crate serde_json;
#[macro_use]
extern crate log;
extern crate fern;

use rumqtt::{MqttOptions, MqttClient, MqttCallback, QoS, Message};
use std::sync::{Arc, RwLock};

mod vision;
mod logging;

use vision::Vision;

fn on_message(message: Message, vision: Arc<RwLock<Vision>>) {
    match message.topic.as_str() {
        "/Vision/RegisterPipeline" => {
            if let Err(e) = vision.write().unwrap().add_pipeline_from_json(message.payload) {
                error!("Failed to add pipeline: {:?}", e);
            }
        },
        _ => warn!("Recieved message from unknown topic: {}", message.topic.as_str()),
    }
}

fn main() {
    let client_opts = MqttOptions::new()
        .set_keep_alive(3)
        .set_reconnect(5)
        .set_client_id("vision")
        .set_broker("localhost:1883");

    let vision = Arc::new(RwLock::new(Vision::new()));

    let vision1 = vision.clone();

    let callback = MqttCallback::new().on_message(move |msg| on_message(msg, vision1.clone()));
    let client = MqttClient::start(client_opts, Some(callback)).expect("Failed to start MQTT client");

}
