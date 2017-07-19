# MARV robotics Docker image
This image is used for storing and sharing bag files easily. [MARV robotics](https://ternaris.com/marv-robotics/) is installed on it.

For some reason the current Dockerfile doesn't work - it creates a broken image. I've installed things manually in a Docker using the virtualenv instructions from [this link](https://github.com/ternaris/marv-robotics)

## Running the image
```
docker run --name "marv" -p 8000:8000 -v /local/path/bags:/bag -d shadowrobot/marv
```

Then connect to [localhost:8000](http://localhost:8000). Adding files to the `/local/path/bags` will make them appear in the website.

For now they can't be added from the webui. The script updating the bag database is `/marv_update.sh` in the commited docker. It's run every 2h from cron.
