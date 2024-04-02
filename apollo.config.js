module.exports = {
  client: {
    service: {
      name: 'lab-hub',
      url: 'http://localhost:8080/graphql'
    },
    includes: ['hub/ui/app/**/*.{ts,tsx}'],
  }
};
