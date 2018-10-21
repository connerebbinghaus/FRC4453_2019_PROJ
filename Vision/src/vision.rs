use std::sync::Arc;

pub struct Vision {

}

impl Vision {
    pub fn new() -> Vision {
        Vision{
        }
    }

    pub fn add_pipeline_from_json(&mut self, data: Arc<Vec<u8>>) -> Result<(), ()> {
        unimplemented!()
    }
}