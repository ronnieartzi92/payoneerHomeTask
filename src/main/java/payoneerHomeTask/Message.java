package payoneerHomeTask;

class Message {
  String id;
  String data;
  Status status;

  Message(String id, String data, Status status) {
    this.id = id;
    this.data = data;
    this.status = status;
  }

  enum Status {
    Accepted,
    Processing,
    Error,
    Complete,
    NotFound
  }

  String getId() {
    return id;
  }

  String getData() {
    return data;
  }

  Status getStatus() {
    return status;
  }

  void setId(String id) {
    this.id = id;
  }

  void setData(String data) {
    this.data = data;
  }

  void setStatus(Status status) {
    this.status = status;
  }

}
