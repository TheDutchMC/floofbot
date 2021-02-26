//rand crate
use rand::Rng;

//serenity crate
use serenity::async_trait;
use serenity::client::{validate_token, Client, Context, EventHandler};
use serenity::model::channel::Message;
use serenity::framework::standard::{
    StandardFramework,
    CommandResult,
    macros::{
        command,
        group
    }
};

use std::process::exit;
use std::path::PathBuf;
use serenity::http::AttachmentType;

#[group]
#[commands(rawr)]
struct General;

struct Handler;

#[async_trait]
impl EventHandler for Handler {}

struct CatPictures {
    paths: Vec<PathBuf>
}

impl serenity::prelude::TypeMapKey for CatPictures {
    type Value = CatPictures;
}

impl CatPictures {
    pub fn new(paths: Vec<PathBuf>) -> CatPictures {
        CatPictures {
            paths
        }
    }
}

#[tokio::main]
async fn main() -> () {
    println!("Loading CatExplorer!");

    //Check BOT_TOKEN environmental variable
    let bot_token_env_var = std::env::var("BOT_TOKEN");
    if bot_token_env_var.is_err() {
        //User did not provide a bot token, we cannot continue
        eprintln!("Environmental variable 'BOT_TOKEN' is not set. Exiting");
        exit(1);
    }
    let bot_token = bot_token_env_var.unwrap();

    //Validate the provided token
    if validate_token(bot_token.as_str()).is_err() {
        eprintln!("The provided bot token is not valid. Please check your configuration. Exiting");
        exit(1);
    }

    //Check the BOT_PREFIX environmental variable
    let bot_prefix_env_var = std::env::var("BOT_PREFIX");
    let bot_prefix: String;
    if bot_prefix_env_var.is_err() {
        //User did not supply a prefix, use the default of '$'
        println!("Environmental variable 'BOT_PREFIX' not set. Using default prefix '$'");
        bot_prefix = "$".to_string();
    } else {
        bot_prefix = bot_prefix_env_var.unwrap();
    }

    //Read the /data directory, this is where the cat pictures are stored
    let mut cat_paths = Vec::new();
    for entry in glob::glob("/data/*").expect("Failed to read glob pattern") {
        match entry {
            Ok(path) => {
                cat_paths.push(path)
            },
            Err(e) => eprintln!("{:?}", e),
        }
    }

    println!("Discovered {} cat picture(s)!", cat_paths.len());
    println!("Starting bot...");

    //Start the bot
    start_bot(bot_token, bot_prefix, cat_paths).await;
}

async fn start_bot(bot_token: String, bot_prefix: String, cat_paths: Vec<PathBuf>) {
    //Create the StandardFramework
    let framework = StandardFramework::new()
        .configure(|c| c.prefix(bot_prefix.as_str()))
        .group(&GENERAL_GROUP);

    //Create the bot client
    let mut client = Client::builder(bot_token)
        .framework(framework)
        .event_handler(Handler)
        .await
        .expect("Unable to create Discord client");

    //In brackets because scope
    {
        let mut data = client.data.write().await;
        data.insert::<CatPictures>(CatPictures::new(cat_paths));
    }

    println!("Startup complete!");

    if let Err(why) = client.start().await {
        eprintln!("An error occurred while running the client: {:?}", why);
    }
}

#[command]
async fn rawr(ctx: &Context, msg: &Message) -> CommandResult {
    //Send a message indicating that we're preparing the cat picture
    msg.channel_id.send_message(ctx, |m| {
        m.content("Preparing floof...");

        m
    }).await.unwrap();

    //Get the cat pictures
    let data = ctx.data.read().await;
    let cat_pictures = data.get::<CatPictures>().unwrap().clone();

    //Take a random integer between 0 and the size of the paths Vector
    let cat_pictures_count = cat_pictures.paths.len();
    let random_integer: usize = rand::thread_rng().gen_range(0..cat_pictures_count);

    //Get the picture at the random integer
    let pic = cat_pictures.paths.get(random_integer).expect("Error, pic not found!").clone();

    //Convert the picture path to a String, then remove /data/ from the Stirng. This leaves behind
    //just the filename. So '/data/example.png' becomes 'example.png'
    //Then concat it with 'attachment://'
    let attachment_name: String = ["attachment://", pic.as_path().to_str().unwrap().replace("/data/", "").as_str()].concat();

    //Create message, create an embed, and send the message
    msg.channel_id.send_message(&ctx.http, |m| {
        m.embed(|e| {
            e.title(format!("Floof number {}/{}:", random_integer+1, cat_pictures_count));
            e.color(0xa1a1a1); //Gray-ish color
            e.image(attachment_name);

            //See comment for 'return m;'
            return e;
        });
        m.add_file(AttachmentType::Path(&pic));

        //Yes I know you can just do 'm', but this is more clear in my opinion
        return m;
    }).await.unwrap();

    Ok(())
}